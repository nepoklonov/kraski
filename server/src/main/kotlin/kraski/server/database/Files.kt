package kraski.server.database

import kraski.common.FileAnswer
import kraski.common.interpretation.*
import kraski.common.models.ImageVersion
import kraski.common.models.ParticipantFile
import kraski.common.models.StaticFile
import kraski.common.models.participants.ArtParticipant
import kraski.common.models.participants.FormType
import kraski.common.models.participants.Participant
import kraski.server.liveFile
import kraski.server.scaleImageByRect
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

fun addParticipantFile(file: ParticipantFile): Int = loggedTransaction {
    ParticipantFile::class.getModelTable().let {
        it.insert(file) get it[ParticipantFile::id]
    }
}

fun addStaticFile(file: StaticFile): Int = loggedTransaction {
    StaticFile::class.getModelTable().let {
        it.insert(file) get it[StaticFile::id]
    }
}

fun getStaticFileIdByRef(fileRef: FileRef): Int? = loggedTransaction {
    StaticFile::class.getModelTable().let { table ->
        table.select {
            (table[StaticFile::dirPath] eq fileRef.dir.path) and (table[StaticFile::fileName] eq fileRef.fileName)
        }.firstOrNull { true }?.let { it[table[StaticFile::id]] }
    }
}

fun getParticipantFileRefById(id: Int) = ParticipantFile::class.getModelTable().run {
    val l = select { get(ParticipantFile::id) eq id }
    val ll = l.first()
    ll.toModel().fileRef
}

fun createImageVersion(originalImage: BufferedImage, scale: Scale, copiesDir: DirRef): BufferedImage {
    val newImage = scaleImageByRect(originalImage, scale)
    val newPath = copiesDir file newImage.run { "${width x height}" } dot "png"
    ImageIO.write(newImage, "PNG", newPath.liveFile)
    return newImage
}

fun addImageVersions(originalFileRef: FileRef, id: Int) {
    val originalImageFile = originalFileRef.liveFile
    val pureName = originalImageFile.nameWithoutExtension
    val originalImage = ImageIO.read(originalImageFile)
    loggedTransaction {
        ImageVersion.sizes.forEach { scale ->
            if (originalImage.run { width x height } > scale) {
                val newImage = createImageVersion(originalImage,
                        scale, originalFileRef.dir / "copies" / pureName)
                ImageVersion::class.getModelTable().insert(
                        ImageVersion(id, newImage.width, newImage.height)
                )
            }
        }
    }
}

//if ((resultSize < scale && size > resultSize) || (resultSize > size && size >= scale)) {
//is it necessary?

fun chooseBestImageVersionSize(versionSizes: List<PlanarSize>, originalRef: FileRef, scale: Scale): FileRef {
//    print ("for ${originalRef.fileName} and $versionSizes choose by scale ${scale.size}")
    var resultRef = originalRef
    if (versionSizes.count() > 0) {
        var resultSize = Int.MAX_VALUE x Int.MAX_VALUE
        val copiesDir = originalRef.run { dir / "copies" / liveFile.nameWithoutExtension }
        for (size in versionSizes) {
            if ((resultSize > size && size >= scale)) {
                resultSize = size
                resultRef = copiesDir file "$resultSize" dot "png"
            }
        }
    }
//    println(" and i chose ${resultRef.path}")
    return resultRef
}

fun getImageVersion(fileId: Int, scale: Scale) = loggedTransaction {
    val mainRef = getParticipantFileRefById(fileId)
    val versionSizes = ImageVersion::class.getModelTable().run {
        selectModels { get(ImageVersion::fileId) eq fileId }
    }.map { it.width x it.height }
    chooseBestImageVersionSize(versionSizes, mainRef, scale).path
}

fun getAllImages(formType: FormType, scale: Scale, bigScale: Scale = 1000 x 700 put ScaleType.INSIDE): List<FileAnswer> = loggedTransaction {
    val participants = formType.klass.getModelTable()
    val files = ParticipantFile::class.getModelTable()
    val imageVersions = ImageVersion::class.getModelTable()
    participants.innerJoin(files, { participants.get<Int>("imageFileId") }, { files[ParticipantFile::id] })
            .innerJoin(imageVersions, { files[ParticipantFile::id] }, { imageVersions[ImageVersion::fileId] })
            .slice(files[ParticipantFile::id], files[ParticipantFile::name],
                    imageVersions[ImageVersion::width], imageVersions[ImageVersion::height])
            .select {
                files[ParticipantFile::id] eq participants["imageFileId"] and
                        participants["imageFileId"] eq imageVersions[ImageVersion::fileId]
            }
            .orderBy(participants[Participant::id], SortOrder.DESC).asSequence()
            .groupBy(keySelector = {
                FileAnswer(it[files[ParticipantFile::id]], it[files[ParticipantFile::name]])
            }, valueTransform = {
                it[imageVersions[ImageVersion::width]] x it[imageVersions[ImageVersion::height]]
            }).map { (file, sizes) ->
                FileAnswer(file.id, chooseBestImageVersionSize(sizes, formType.folder file file.src, scale).path,
                        chooseBestImageVersionSize(sizes, formType.folder file file.src, bigScale).path, getOpenParticipantInfo(formType, file.id))
            }
}


fun getOpenParticipantInfo(formType: FormType, fileId: Int): List<String> {
    val list = mutableListOf<String>()
    transaction {
        addLogger(StdOutSqlLogger)
        val participantTable = formType.klass.getModelTable()
        val participant = participantTable.select { participantTable.get<Int>("imageFileId") eq fileId }.first()
        list += participant[participantTable.get<String>("title")]
        //TODO razgresti govnoCode
        when (formType) {
            FormType.Art, FormType.Photos -> {
                list += participant[participantTable.get<String>("surname")] + " " +
                        participant[participantTable.get<String>("name")] + " " +
                        participant[participantTable.get<Int>("age")].toString()
                list += ""
            }
//            FormType.Dance, FormType.Music -> {
//                if (participant[participantTable.get<String>("hasGroup")] == "Single") {
//                    list += participant[participantTable.get<String>("surname")] + " " +
//                        participant[participantTable.get<String>("name")]
//                    list += participant[participantTable.get<Int>("age")].toString()
//                } else {
//                    list += participant[participantTable.get<String>("group")]
//                    list += ""
//                }
//            }
//
//            FormType.Professional, FormType.Scientific, FormType.Literature -> {
//                list += participant[participantTable.get<String>("patronymic")]
//                list += ""
//            }
            else -> error("1234567898765432")
        }
        list += participant[participantTable.get<String>("city")]
    }
    return list
}


fun getOriginal(fileId: Int): String {
    return transaction {
        addLogger(StdOutSqlLogger)
        val participantFileTable = ParticipantFile::class.getModelTable()
        val mainRef = participantFileTable.run {
            select { get<Int>("id") eq fileId }.first().let {
                FormType.valueOf(it[get("formType")]).folder file it[get("name")]
            }
        }
        mainRef.path
    }
}


fun dataGetAll(formType: FormType): List<String> {
    return transaction {
        addLogger(StdOutSqlLogger)
        val participantTable = formType.klass.getModelTable()
        participantTable.selectAll().orderBy(participantTable.get<Int>("id"), SortOrder.DESC).mapNotNull { participant ->
            var list = ""
            list += participant[participantTable.get<String>("title")] + " — "
            when (formType) {
                FormType.Dance, FormType.Music -> {
                    if (participant[participantTable.get<String>("hasGroup")] == "Single") {
                        list += participant[participantTable.get<String>("surname")] + " " +
                                participant[participantTable.get<String>("name")] + ", "
                        list += participant[participantTable.get<Int>("age")].toString() + " лет, "
                    } else {
                        list += participant[participantTable.get<String>("group")] + ", "
                        list += ""
                    }
                }

                FormType.Professional, FormType.Scientific -> {
                    list += participant[participantTable.get<String>("surname")] + " " +
                            participant[participantTable.get<String>("name")] + " " +
                            participant[participantTable.get<String>("patronymic")] + ", "
                }
                FormType.Literature -> {
                    list += participant[participantTable.get<String>("surname")] + " " +
                            participant[participantTable.get<String>("name")] + ", "
                    list += participant[participantTable.get<Int>("age")].toString() + " лет, "
                }

                else -> error("89066 8")
            }
            list += participant[participantTable.get<String>("city")]
            list
        }
    }
}


var d = mutableMapOf<Int, Pair<Long, Long>>()

fun ein(vararg str: Int) {
    str.forEach {
        d[it] = java.util.Date().time to 0L
    }
}

fun zwei(str: Int) {
    d[str] = java.util.Date().time to (d[str]?.second ?: 0L)
}

fun drei(str: Int) {
    d[str] = java.util.Date().let {
        it.time to d[str]!!.second + it.time - d[str]!!.first
    }
}

fun fier() {
    d.forEach { (key, value) ->
        println("operation $key: ${value.second}")
    }
    d = mutableMapOf()
}