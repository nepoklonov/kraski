package kraski.common.models.participants

import kraski.common.DisplayType
import kraski.common.Display
import kraski.common.SaveToTable
import kraski.common.Validation
import kotlinx.serialization.Serializable

@Serializable
data class ProfessionalParticipant (

        @SaveToTable
        @Display(105, "Отчество", width = 30)
        val patronymic: String,

        @SaveToTable
        @Display(106, "Название учреждения", width = 45)
        val organization: String,

        @SaveToTable
        @Display(107, "Должность", width = 45)
        val job: String,

        @SaveToTable
        @Display(204, "Номер телефона", width = 45)
        val phone: String,

        @SaveToTable
        @Display(211, "Название работы", width = 45)
        val title: String,

        @SaveToTable
        @Display(214, "Приложить файл",
                displayType = DisplayType.File, validation = Validation.EssayFileName,
                width = 50)
        val fileId: Int,

        override val id: Int, override val time: String, override val surname: String, override var name: String, override val email: String, override val agree: Unit, override val know: Unit, override val submit: Unit, override val city: String

) : Participant, OnlyName, NotOrganizer