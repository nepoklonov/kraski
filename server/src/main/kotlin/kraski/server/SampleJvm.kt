package kraski.server

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.http.content.file
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.routing.Route
import io.ktor.routing.routing
import io.ktor.sessions.*
import io.ktor.util.KtorExperimentalAPI
import kraski.server.api.startAPI
import kraski.server.database.startDB
import kraski.server.generated.generateLoadingHTML
import kraski.server.generated.generateStylesCSS

fun Route.openFolder(folderName: String) {
    static("/$folderName") {
        files(folderName)
    }
}

fun Route.openFolders(vararg folderNames: String) {
    for (folderName in folderNames) {
        openFolder(folderName)
    }
}

@KtorExperimentalAPI
fun Application.main() {
    println("Server started!")
    startDB()
    startImagesConnection()
    install(CORS) {
        anyHost()
        allowCredentials = true
    }
    install(Sessions) {
        cookie<UserSession> ("user-session", SessionStorageMemory()) {
            cookie.path = "/"
        }
    }
    routing {
        openFolders("images", "smi", "documents", "fonts", "yaml", "uploads")

        static("/") {
            file("main.bundle.js")
        }

        generateLoadingHTML("{...}")

        generateStylesCSS("/styles.css")

        startAPI()
    }
}