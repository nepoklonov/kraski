package kraski.server.api.join

import io.ktor.routing.Route
import kraski.common.*
import kraski.common.interpretation.Pages
import kraski.common.models.ParticipantFile
import kraski.server.api.listenAndAutoRespond
import kraski.server.database.addImageVersions
import kraski.server.database.addParticipantFile
import kotlinx.serialization.serializer
import kraski.common.Answer
import kraski.common.Method
import kraski.common.Request
import kraski.server.liveFile
import java.io.File


fun Route.startFileUploadAPI() = listenAndAutoRespond<Request.FileUpload>(Method.FileUpload) { request, files ->
    if (files.isNotEmpty()) {
        val dir = Pages.Uploads.temp
        try {
            val fileData = request.filesData.first()
            val resultDir = request.formType.folder
            val ext = File(fileData.originalFileName).extension
            val newName = fileData.namePrefix usc randomString(7) dot ext
            val newFileRef = resultDir file newName
            (dir file files[0]).liveFile.renameTo(newFileRef.liveFile)
            val id = addParticipantFile(ParticipantFile(-1, request.formType, newName, fileData.originalFileName))
            if (fileData.isImage) addImageVersions(newFileRef, id)
            Answer.ok(Int.serializer(), id)
        } catch (e: NoSuchElementException) {
            Answer.wrong("Necessary file info is missing")
        }
    } else {
        Answer.wrong("File is missing")
    }
}