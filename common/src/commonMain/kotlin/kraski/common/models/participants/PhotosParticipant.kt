package kraski.common.models.participants

import kotlinx.serialization.Serializable
import kraski.common.*

@Serializable
data class PhotosParticipant(
        @SaveToTable
        @Display(107, "Возраст", validation = Validation.Number, width = 30)
        val age: Int,

        @SaveToTable
        @Display(151, "Образовательное учреждение", width = 45, validation = Validation.Any)
        val organization: String,

        @SaveToTable
        @Display(204, "Номер телефона", width = 45)
        val phone: String,

        @SaveToTable
        @Display(210, "Название работы", width = 45)
        val title: String,

        @SaveToTable
        @Display(211, "Загрузите файл работы",
                displayType = DisplayType.File, validation = Validation.EssayFileName,
                width = 45)
        val imageFileId: Int,

        override val id: Int, override val time: String, override val submit: Unit, override val email: String, override val agree: Unit, override val surname: String, override var name: String, override val know: Unit, override val city: String, override val supervisor: Boolean, override val supervisorFIO: String, override val supervisorContacts: String
) : Participant, NotOrganizer, OnlyName, WithSupervisor