package kraski.client

import kraski.common.Answer
import kraski.common.Request
import kotlinx.serialization.*
import org.w3c.files.Blob
import org.w3c.xhr.FormData
import org.w3c.xhr.XMLHttpRequest
import kraski.common.json
import kraski.common.serialize
import kotlinx.coroutines.*
import kotlin.browser.window
import kotlin.coroutines.resume

inline fun callAPI(destination: String, params: FormData, crossinline action: XMLHttpRequest.() -> Unit) {
    val url = "${window.location.origin}/$destination"
    val xHTTP = XMLHttpRequest()
    xHTTP.open("POST", url)
    xHTTP.onreadystatechange = {
        if (xHTTP.readyState == 4.toShort()) {
            xHTTP.action()
        }
    }
    xHTTP.send(params)
}

suspend fun callAPI(destination: String, params: FormData): String = suspendCancellableCoroutine { cont ->
    val url = "${window.location.origin}/$destination"
    val xHTTP = XMLHttpRequest()
    xHTTP.open("POST", url)
    xHTTP.onreadystatechange = {
        if (xHTTP.readyState == 4.toShort()) {
            cont.resume(xHTTP.responseText)
        }
    }
    xHTTP.send(params)
}

fun String.toFormData(): FormData =
    FormData().apply {
        append("request", this@toFormData)
    }

class JSFile(val name: String, val value: Blob, val filename: String)

inline fun <reified R : Request> R.send(files: List<JSFile> = listOf(), crossinline action: (String) -> Unit) {
    callAPI(method.methodName, serialize().toFormData().apply {
        files.forEach {
            append(it.name, it.value, it.filename)
        }
    }) {
        action(responseText)
    }
}

inline fun <reified R : Request, A> R.send(
    kSerializer: KSerializer<A>,
    files: List<JSFile> = listOf(),
    crossinline action: (A) -> Unit
) = send(files) {
    val answer = json.parse(Answer.serializer(), it)
    action(json.parse(kSerializer, answer.body))
}

inline fun <reified R : Request, A> R.send(
    kSerializer: KSerializer<A>,
    crossinline action: (A) -> Unit
) = send(kSerializer, listOf(), action)


suspend inline fun <reified R : Request> R.send(files: List<JSFile> = listOf()): String =
    callAPI(method.methodName, serialize().toFormData().apply {
        files.forEach {
            append(it.name, it.value, it.filename)
        }
    })

fun String.parseAnswer() = json.parse(Answer.serializer(), this)

fun <A> Answer.parseBody(kSerializer: KSerializer<A>) = json.parse(kSerializer, body)

fun <A> String.parseAnswerBody(kSerializer: KSerializer<A>) = parseAnswer().parseBody(kSerializer)


//fun <T : ScaleContainer> Image<T>.getImageRealSrc(scale: T): String {
//    Request.StaticImagesGetVersion(this, scale).send()
//}

//fun <T : ScaleContainer> Image<T>.getImageRealSrc(): String {
//    return getImageRealSrc(scales[0])
//}