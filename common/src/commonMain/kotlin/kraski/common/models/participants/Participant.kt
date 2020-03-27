package kraski.common.models.participants

import kraski.common.*
import kraski.common.DisplayType.*
import kraski.common.Validation.*
import kraski.common.interpretation.DirRef
import kraski.common.interpretation.Pages
import kraski.common.models.AdminLogin
import kraski.common.models.News
import kraski.common.models.Review
import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass


enum class FormType(
    val klass: KClass<out AnyForm>,
    val serializer: KSerializer<out AnyForm>,
    val folder: DirRef,
    val title: String
) {

    Art(ArtParticipant::class, ArtParticipant.serializer(), Pages.Uploads.Images.art, "Конкурс художественного творчества имени А. А. Кокеля"),
    Photos(PhotosParticipant::class, PhotosParticipant.serializer(), Pages.Uploads.Images.photos, "Конкурс фотомастерства имени Г. А. и А. А. Костиных"),
    Music(MusicParticipant::class, MusicParticipant.serializer(), Pages.Uploads.Other.music, "Конкурс вокально-инструментального творчества имени М. Д. Михайлова"),
    Dance(DanceParticipant::class, DanceParticipant.serializer(), Pages.Uploads.Other.dance, "Конкурс танцевального творчества имени Н. В. Павловой"),

    Professional(ProfessionalParticipant::class, ProfessionalParticipant.serializer(), Pages.Uploads.Other.professional, "Конкурс профессионального мастерства работников учреждений культуры и образования имени Г. Н. Волкова"),
    Scientific(ScientificParticipant::class, ScientificParticipant.serializer(), Pages.Uploads.Other.scientific, "Конкурс исследований в области краеведения и генеалогии имени Н. В. Никольского"),
    Literature(LiteratureParticipant::class, LiteratureParticipant.serializer(), Pages.Uploads.Other.literature, "Литературный конкурс имени К. В. Иванова"),

    ReviewForm(Review::class, Review.serializer(), DirRef.root, "Отзывы"),
    NewsForm(News::class, News.serializer(), Pages.Uploads.Images.news, "Новости"),
    Admin(AdminLogin::class, AdminLogin.serializer(), DirRef.root, "Админка");

    val getSerializer: KSerializer<out AnyForm>
        get() = klass.getSerializer()

    val haveAnImage: Boolean
        get() = when (this) {
            Photos, Music, Dance -> true
            else -> false
        }
}

interface AnyForm {
    @SaveToTable(autoIncremented = true, isPrimaryKey = true)
    val id: Int

    @SaveToTable(forceValidation = Validation.Text)
    val time: String

    @Display(50003, "", displayType = Submit, validation = Any, width = 40)
    val submit: Unit
}

interface Participant : AnyForm {
    @SaveToTable
    @Display(200, "E-mail", validation = Email, width = 45)
    val email: String

    @Display(10001, "$1", displayType = CheckBox, validation = FilledCheckBox)
    val agree: Unit
}

interface WithSupervisor {
    @SaveToTable
    @Display(206, "Есть куратор/преподаватель", displayType = DisplayType.CheckBox, validation = AnyCheckBox)
    val supervisor: Boolean

    @SaveToTable
    @Display(207, "ФИО куратора/преподавателя")
    @Belongs(OwnType.ByCheckBox, ["supervisor", "1"])
    val supervisorFIO: String

    @SaveToTable
    @Display(208, "Контактные данные куратора/преподавателя")
    @Belongs(OwnType.ByCheckBox, ["supervisor", "1"])
    val supervisorContacts: String
}

interface OnlyName {
    @SaveToTable
    @Display(101, "Фамилия", width = 30)
    val surname: String

    @SaveToTable
    @Display(102, "Имя", width = 30)
    var name: String
}

interface NameOrGroup {
    @SaveToTable
    @Display(100, "Коллективная работа", displayType = DisplayType.Radio, validation = Validation.Radio)
    val hasGroup: HowMuch

    @SaveToTable
    @Display(101, "Название коллектива/список участников через запятую")
    @Belongs(OwnType.BySelect, ["hasGroup", "1"])
    val group: String

    @SaveToTable
    @Display(101, "Фамилия", width = 30)
    @Belongs(OwnType.BySelect, ["hasGroup", "0"])
    val surname: String

    @SaveToTable
    @Display(102, "Имя", width = 30)
    @Belongs(OwnType.BySelect, ["hasGroup", "0"])
    var name: String
}

interface NotOrganizer {
    @Display(10002, "$2", displayType = CheckBox, validation = FilledCheckBox)
    val know: Unit

    @SaveToTable
    @Display(150, "Населённый пункт", width = 45)
    val city: String
}

enum class HowMuch(override val title: String) : Titled {
    Single("Один участник"),
    Plural("Коллектив")
}