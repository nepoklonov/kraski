package kraski.common.models

import kraski.common.Display
import kraski.common.DisplayType
import kraski.common.SaveToTable
import kraski.common.Validation
import kraski.common.models.participants.AnyForm
import kotlinx.serialization.Serializable

@Serializable
data class Raskraska(
        @SaveToTable
        @Display(103, "Название раскраски")
        val header: String,

        @SaveToTable
        @Display(212, "Раскраска",
                displayType = DisplayType.File, validation = Validation.Any,
                width = 45)
        val imageFileId: Int,

//        @SaveToTable
//        @Display(213, "Оригинал",
//                displayType = DisplayType.File, validation = Validation.Any,
//                width = 45)
//        val imageFileId: Int,


        override val id: Int, override val time: String, override val submit: Unit
) : AnyForm

@Serializable
data class RaskraskaWithSrc(
        val raskraska: Raskraska,
        val src: String
)