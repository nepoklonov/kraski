package kraski.common.models

import kraski.common.Display
import kraski.common.models.participants.AnyForm
import kotlinx.serialization.Serializable

@Serializable
data class AdminLogin(
    @Display(101, "Секретные слова")
    val password: String,

    override val id: Int, override val time: String, override val submit: Unit
) : AnyForm