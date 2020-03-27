package kraski.common.models.participants

import kraski.common.*
import kotlinx.serialization.Serializable


@Serializable
data class MusicParticipant(
        @SaveToTable
        @Display(105, "Возраст", validation = Validation.Number, width = 30)
        @Belongs(OwnType.BySelect, ["hasGroup", "0"])
        val age: Int,

        @SaveToTable
        @Display(151, "Образовательное учреждение", validation = Validation.Any, width = 45)
        val organization: String,

        @SaveToTable
        @Display(204, "Номер телефона", width = 45)
        val phone: String,

        @SaveToTable
        @Display(211, "Название номера/работы", width = 45)
        val title: String,

        @SaveToTable
        @Display(212, "Загрузите работу",
                displayType = DisplayType.File, validation = Validation.EssayFileName,
                width = 45)
        val mediaFileId: Int,
        override val id: Int, override val time: String, override val agree: Unit, override val know: Unit, override val submit: Unit, override val surname: String, override var name: String, override val email: String, override val city: String, override val hasGroup: HowMuch, override val group: String, override val supervisor: Boolean, override val supervisorFIO: String, override val supervisorContacts: String
) : Participant, NameOrGroup, NotOrganizer, WithSupervisor
