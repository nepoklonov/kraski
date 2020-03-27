package kraski.server.api
import io.ktor.routing.Route
import kotlinx.serialization.KSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kraski.common.*
import java.io.File

@Suppress("UNCHECKED_CAST")
fun Route.startGetJsonAPI() = listenAndAutoRespond<Request.GetJson>(Method.GetJson) { request, _ ->
    val text = File(request.jsonRef.getFileRefByName().path.trim('/')).readText()
    val json = Json.parse(request.jsonRef.serializer, text)
    Answer.ok(request.jsonRef.serializer as KSerializer<Any>, json)
}