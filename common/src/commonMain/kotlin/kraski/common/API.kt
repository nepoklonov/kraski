package kraski.common

import kraski.common.interpretation.FileRef
import kraski.common.interpretation.Scale
import kraski.common.interpretation.JsonRef
import kraski.common.models.InputField
import kraski.common.models.participants.FormType
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

enum class Method(val methodName: String, val fileTransfer: Boolean = false) {
    GetJson("api/get-json"),
    GetModel("api/join/get-model"),
    FileUpload("api/join/file-upload", true),
    FormSend("api/join/form-send"),
    StaticImagesGetVersion("api/static/images/get-version"),
    ParticipantsImagesGetAll("api/participants/images/get-all"),
    ParticipantsImagesGetInfo("api/participants/images/get-info"),
    ParticipantsImagesGetVersion("api/participants/images/get-version"),
    ParticipantsImagesGetOriginal("api/participants/images/get-original"),
    ParticipantsDataGetAll("api/participants/ethno-tour/get-all"),
    GetGeneralInfo("api/get-general-info"),
    AboutGetPhotos("api/about/get-photos"),
    NewsGetAll("api/news/get-all"),
    ReviewsGetAll("api/reviews/get-all"),
    CWNewsGetAll("api/cw/news/get-all"),
    AdminParticipantsGetAll("api/admin/participants/get-all"),
    AdminGetFileById("api/admin/get-file"),
    AdminCheck("api/admin/check")
}

enum class FileType {
    Image, Other;
}

@Serializable
data class FileData(
        val fileType: FileType,
        val originalFileName: String,
        val namePrefix: String
) {
    val isImage: Boolean
        get() = fileType == FileType.Image
}

@UseExperimental(ImplicitReflectionSerializer::class)
inline fun <reified T: Any> T.serialize() = json.stringify(T::class.serializer(), this)
@UseExperimental(ImplicitReflectionSerializer::class)
inline fun <reified T: Any> String.deserialize() = json.parse(T::class.serializer(), this)

@UseExperimental(ImplicitReflectionSerializer::class)
fun <T: Any> KClass<T>.getSerializer() = serializer()

@Serializable
sealed class Request(val method: Method) {

    @Serializable
    class GetJson(val jsonRef: JsonRef) : Request(Method.GetJson)

    @Serializable
    class StaticImagesGetVersion(val image: FileRef, val scale: Scale) :
        Request(Method.StaticImagesGetVersion)

    @Serializable
    class ParticipantsImagesGetInfo(val formType: FormType, val fileIndex: Int) :
        Request(Method.ParticipantsImagesGetInfo)

    @Serializable
    class ParticipantsImagesGetVersion(val fileIndex: Int, val scale: Scale) :
        Request(Method.ParticipantsImagesGetVersion)

    @Serializable
    class ParticipantsImagesGetAll(val formType: FormType, val scale: Scale) :
        Request(Method.ParticipantsImagesGetAll)

    @Serializable
    class ParticipantsImagesGetOriginal(val fileIndex: Int) :
        Request(Method.ParticipantsImagesGetOriginal)

    @Serializable
    class ParticipantsDataGetAll(val formType: FormType) : Request(Method.ParticipantsDataGetAll)

    @Serializable
    class FileUpload(val formType: FormType, val filesData: List<FileData>) : Request(Method.FileUpload) {
        constructor(formType: FormType, fileData: FileData) : this(formType, listOf(fileData))
    }

    @Serializable
    class FormSend(val formType: FormType, val list: List<InputField>, val id: Int? = null) : Request(Method.FormSend)

    @Serializable
    class GetModel(val formName: FormType, val id: Int? = null) : Request(Method.GetModel)

    @Serializable
    class GetGeneralInfo: Request(Method.GetGeneralInfo)

    @Serializable
    class AboutGetPhotos(val width: Int, val height: Int): Request(Method.AboutGetPhotos)

    @Serializable
    class NewsGetAll(val width: Int, val height: Int): Request(Method.NewsGetAll)

    @Serializable
    class ReviewsGetAll: Request(Method.ReviewsGetAll)

    @Serializable
    class CWNewsGetAll(val width: Int, val height: Int): Request(Method.CWNewsGetAll)

    @Serializable
    class AdminParticipantGetAll(val formType: FormType): Request(Method.AdminParticipantsGetAll)

    @Serializable
    class AdminGetFileById(val id: Int): Request(Method.AdminGetFileById)

    @Serializable
    class AdminCheck: Request(Method.AdminCheck)
}

enum class AnswerType(val code: Int) {
    OK(200), WRONG(200), AccessDenied(403)
}

@Serializable
class Answer private constructor(val answerType: AnswerType, val body: String) {
    companion object {
        fun wrong(message: String = "") = Answer(AnswerType.WRONG, message.serialize())
        fun ok(message: String = "") = Answer(AnswerType.OK, message.serialize())
        fun accessDenied() = Answer(AnswerType.AccessDenied, "")
        fun <T: Any> ok(serializer: KSerializer<T>, body: T) = Answer(AnswerType.OK, json.stringify(serializer, body))
    }
}

@Serializable
data class FileAnswer(
    val id: Int,
    val src: String,
    val bigSrc: String? = null,
    val info: List<String>? = null
)

@Serializable
class SubGallery(val title: String, val list: List<FileAnswer>)

@Serializable
class GeneralInfo(
    val participantsAmount: Int = 0,
    val citiesAmount: Int = 0
)