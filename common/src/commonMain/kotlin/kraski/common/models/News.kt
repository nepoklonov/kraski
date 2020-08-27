package kraski.common.models

import kraski.common.Display
import kraski.common.DisplayType
import kraski.common.SaveToTable
import kraski.common.Validation
import kraski.common.models.participants.AnyForm
import kotlinx.serialization.Serializable

@Serializable
data class News(
    @SaveToTable
    @Display(103, "Заголовок")
    val header: String,

    @SaveToTable
    @Display(104, "Короткая форма новости")
    val shortContent: String,

    @SaveToTable(longText = true)
    @Display(105, "Текст новости", displayType = DisplayType.HTML, validation = Validation.LongText)
    val content: String,

    @SaveToTable
    @Display(106, "Автор")
    val author: String,

    @SaveToTable
    @Display(212, "Картинка (по желанию)",
        displayType = DisplayType.File, validation = Validation.Any,
        width = 45)
    val imageFileId: Int,

    @SaveToTable
    @Display(213, "Дата (в формате DD.MM.YYYY)", validation = Validation.Date, width = 45)
    val date: String,

    override val id: Int, override val time: String, override val submit: Unit
) : AnyForm

@Serializable
data class NewsWithSrc(
    val news: News,
    val src: String
)