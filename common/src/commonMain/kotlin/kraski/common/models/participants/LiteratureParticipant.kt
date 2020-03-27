package kraski.common.models.participants

import kraski.common.*
import kotlinx.serialization.Serializable
import kraski.common.Display
import kraski.common.DisplayType
import kraski.common.SaveToTable

@Serializable
data class LiteratureParticipant(
        @SaveToTable
        @Display(1, "Номинация", displayType = DisplayType.Select)
        val nomination: Nomination,

        @SaveToTable
        @Display(107, "Возраст", validation = Validation.Number, width = 45)
        val age: Int,

        @SaveToTable
        @Display(151, "Образовательное учреждение (если есть)", width = 45, validation = Validation.Any)
        val organization: String,

        @SaveToTable
        @Display(211, "Название работы", width = 45)
        val title: String,

        @SaveToTable
        @Display(214, "Приложить файл",
                displayType = DisplayType.File, validation = Validation.EssayFileName,
                width = 50)
        val fileId: Int,

        override val id: Int, override val time: String, override val surname: String, override var name: String, override val email: String, override val agree: Unit, override val know: Unit, override val submit: Unit, override val city: String
) : Participant, NotOrganizer, OnlyName {

    enum class Nomination(override val title: String) : Titled {
        Poetry("Поэзия"),
        Prose("Проза"),
        Declaim("Декламация")
    }
}