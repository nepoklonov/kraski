package kraski.server.api

import io.ktor.routing.Route
import kraski.common.interpretation.ImageDirs
import kraski.common.interpretation.ScaleType
import kraski.common.interpretation.put
import kraski.common.interpretation.x
import kotlinx.serialization.list
import kotlinx.serialization.serializer
import kraski.common.*
import kraski.server.categoryImagesGetVersion
import kraski.server.database.*
import kraski.server.liveFile
import kraski.server.staticImageGetVersion

fun Route.startImagesGetVersionAPI() = listenAndAutoRespond<Request.ParticipantsImagesGetVersion>(Method.ParticipantsImagesGetVersion) { request, _ ->
    val l = getImageVersion(request.fileIndex, request.scale)
    Answer.ok(l)
}

fun Route.startImagesGetAllAPI() = listenAndAutoRespond<Request.ParticipantsImagesGetAll>(Method.ParticipantsImagesGetAll) { request, _ ->
    val list = getAllImages(request.formType, request.scale)
    println(list)
    Answer.ok(FileAnswer.serializer().list, list)
}

fun Route.startImagesGetInfoAPI() = listenAndAutoRespond<Request.ParticipantsImagesGetInfo>(Method.ParticipantsImagesGetInfo) { request, _ ->
    val list = getOpenParticipantInfo(request.formType, request.fileIndex)
    Answer.ok(String.serializer().list, list)
}

fun Route.startImagesGetOriginalAPI() = listenAndAutoRespond<Request.ParticipantsImagesGetOriginal>(Method.ParticipantsImagesGetOriginal)
{ request, _ ->
    Answer.ok(getOriginal(request.fileIndex))
}

fun Route.startDataGetAllAPI() = listenAndAutoRespond<Request.ParticipantsDataGetAll>(Method.ParticipantsDataGetAll) { request, _ ->
    Answer.ok(String.serializer().list, dataGetAll(request.formType))
}

fun Route.startAboutPhotosAPI() = listenAndAutoRespond<Request.AboutGetPhotos>(Method.AboutGetPhotos) { _, _ ->

//    val userSession = call.sessions.get<UserSession>() ?: UserSession(Level.JustSomeone)
//    val uS = userSession.copy(level = Level.SysAdmin)
//    println(uS)
//    call.sessions.set(uS)

    val all = categoryImagesGetVersion("old", ImageDirs.getDirectoryScales(ImageDirs.old), 500 x 500 put ScaleType.INSIDE)
    val list = (2006..2019).reversed().map {
        val oldDirRef = ImageDirs.old / "Земля_Калевалы_$it"
        val oldDirFile = oldDirRef.liveFile
        val files = oldDirFile.listFiles()?.toList()?.filter{ !it.isDirectory } ?: listOf()
        SubGallery("$it", files.map { all[oldDirRef file it.name] ?: error("rara") })
    }
    Answer.ok(SubGallery.serializer().list, list)
}

fun Route.startStaticImagesGetVersionAPI() = listenAndAutoRespond<Request.StaticImagesGetVersion>(Method.StaticImagesGetVersion) { request, _ ->
    staticImageGetVersion(request.image, request.scale)?.let {
        Answer.ok(it.path)
    } ?: Answer.wrong("strange image, go away")
}