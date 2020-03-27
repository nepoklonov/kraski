package kraski.common.models

import kraski.common.Display
import kraski.common.SaveToTable
import kraski.common.Validation
import kraski.common.models.participants.AnyForm
import kotlinx.serialization.Serializable

@Serializable
data class Review (
    @SaveToTable
    @Display(103, "ФИО (по желанию)", validation = Validation.Any)
    val fio: String,

    @SaveToTable(longText = true)
    @Display(104, "Текст отзыва")
    val review: String,

    override val id: Int, override val time: String, override val submit: Unit
): AnyForm