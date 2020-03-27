package kraski.server

import kraski.common.FileAnswer
import kraski.common.div
import kraski.common.interpretation.*
import kraski.common.models.StaticFile
import kraski.common.models.StaticImageVersion
import kraski.server.database.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.select
import javax.imageio.ImageIO
import kotlin.reflect.full.memberProperties

val allImages: MutableMap<Int, Image<*>> = mutableMapOf()

fun staticImagesAutoCreateVersions(imageMap: Map<String, ImageData>, category: String, images: List<Image<*>>) {
    images.forEach { image ->
        var l = 0 x 0
        val staticFile = image.original.run { StaticFile(category, dir.path, fileName, 0, 0) }
        val fileId = imageMap[image.original.path]?.id ?: addStaticFile(staticFile.let {
            ImageIO.read(image.original.liveFile).run {
                l = width x height
                it.copy(width = width, height = height)
            }
        })
        allImages[fileId] = image
        val sizes = imageMap[image.original.path]?.sizes ?: listOf()

        val originalSize = imageMap[image.original.path]?.originalSize ?: l

        val realSizes = image.scales.map {
            calculateSizeByScale(originalSize, it.scale)
        }

        StaticImageVersion::class.getModelTable().let {
            loggedTransaction {
                sizes.filter { size -> size !in realSizes }.forEach { size ->
                    it.deleteWhere {
                        (it[StaticImageVersion::fileId] eq fileId) and
                            (it[StaticImageVersion::width] eq size.width) and
                            (it[StaticImageVersion::height] eq size.height)
                    }
                    (staticFile.copiesDirRef file "$size.png").liveFile.delete()
                }
            }
        }

        image.scales.forEach {
            val realSize = calculateSizeByScale(originalSize, it.scale)
            if (originalSize > it.scale) {
                if (realSize !in sizes) {
                    loggedTransaction {
                        StaticImageVersion::class.getModelTable().insert(
                            StaticImageVersion(fileId, realSize.width, realSize.height)
                        )
                    }
                }
                if (!(staticFile.copiesDirRef file "$realSize.png").liveFile.exists()) {
                    createImageVersion(ImageIO.read(image.original.liveFile), it.scale, staticFile.copiesDirRef)
                }
            }
        }
    }
}

fun staticImageGetVersion(fileRef: FileRef, scale: Scale): FileRef? {
    return allImages.entries.firstOrNull {
        it.value.original == fileRef
    }?.key?.let { id ->
        val versionSizes = StaticImageVersion::class.getModelTable().run {
            loggedTransaction {
                selectModels { get(StaticImageVersion::fileId) eq id }
            }
        }.map { it.width x it.height }
        chooseBestImageVersionSize(versionSizes, fileRef, scale)
    }
}

fun categoryImagesGetVersion(category: String, scale: Scale, bigScale: Scale): Map<FileRef, FileAnswer> = loggedTransaction {
    val files = StaticFile::class.getModelTable()
    val versions = StaticImageVersion::class.getModelTable()
    files.innerJoin(versions, { files[StaticFile::id] }, { versions[StaticImageVersion::fileId] })
        .select {
            (files[StaticFile::id] eq versions[StaticImageVersion::fileId]) and
                (files[StaticFile::category] eq category)
        }
        .asSequence()
        .groupBy(keySelector = {
            files.run { it.toModel() }
        }, valueTransform = {
            it[versions[StaticImageVersion::width]] x it[versions[StaticImageVersion::height]]
        }).map { (file, sizes) ->
            val fileRef = DirRef(file.dirPath) file file.fileName
            fileRef to FileAnswer(file.id, chooseBestImageVersionSize(sizes, fileRef, scale).path,
                chooseBestImageVersionSize(sizes, fileRef, bigScale).path)
        }.toMap()

//    return allImages.entries.firstOrNull {
//        it.value.original == fileRef
//    }?.key?.let { id ->
//        val versionSizes = StaticImageVersion::class.getModelTable().run {
//            loggedTransaction {
//                selectModels { get(StaticImageVersion::fileId) eq id }
//            }
//        }.map { it.width x it.height }
//        chooseBestImageVersionSize(versionSizes, fileRef, scale)
//    }
}

fun getImagesFromOld() = (2006..2019).reversed().flatMap { year ->
    val oldDirRef = ImageDirs.old / "Земля_Калевалы_$year"
    val oldDirFile = oldDirRef.liveFile
    val files = oldDirFile.listFiles()?.toList()?.filter { !it.isDirectory } ?: listOf()
    files.map { oldDirRef file it.name }
}

data class ImageData(val id: Int, val originalSize: PlanarSize, val sizes: List<PlanarSize>)

fun startImagesConnection() {
    val images = mapOf(
        "images" to Images::class.memberProperties.mapNotNull { property ->
            property(Images).let { if (it is Image<*>) it else null }
        })
//        "old" to getImagesFromOld().map {
//            ImageDirs.getImageFromDirectory(ImageDirs.old, it)
//        })

    val files = StaticFile::class.getModelTable()
    val versions = StaticImageVersion::class.getModelTable()
    val imagesMap = loggedTransaction {
        (files.innerJoin(versions, { files[StaticFile::id] }, { versions[StaticImageVersion::fileId] }))
            .select {
                (files[StaticFile::id] eq versions[StaticImageVersion::fileId])
            }.groupBy({
                files.run { it.toModel() }
            }, {
                it[versions[StaticImageVersion::width]] x it[versions[StaticImageVersion::height]]
            }).map { (file, sizes) ->
                file.dirPath / file.fileName to ImageData(file.id, file.width x file.height, sizes)
            }.toMap()
    }
    println(imagesMap.toString())
    images.forEach {
        staticImagesAutoCreateVersions(imagesMap, it.key, it.value)
    }
}
