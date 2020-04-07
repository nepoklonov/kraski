package kraski.common.models

import kraski.common.Display
import kraski.common.DisplayType
import kraski.common.SaveToTable
import kraski.common.Validation
import kraski.common.models.participants.AnyForm
import kotlinx.serialization.Serializable

@Serializable
data class Stories(
        @SaveToTable
        @Display(103, "Название сказки")
        val header: String,

        @SaveToTable(longText = true)
        @Display(104, "Описание", displayType = DisplayType.HTML)
        val shortContent: String,

        @SaveToTable
        @Display(105, "Код для вставки сказки")
        val content: String,

        @SaveToTable
        @Display(106, "Автор")
        val author: String,

        override val id: Int, override val time: String, override val submit: Unit
) : AnyForm

@Serializable
data class StoriesWithSrc(
        val stories: Stories,
        val src: String
)