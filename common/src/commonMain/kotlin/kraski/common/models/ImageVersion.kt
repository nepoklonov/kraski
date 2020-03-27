package kraski.common.models

import kraski.common.SaveToTable
import kraski.common.interpretation.ScaleType
import kraski.common.interpretation.put
import kraski.common.interpretation.x
import kotlinx.serialization.Serializable

interface ImageVersionCommon {
    @SaveToTable(autoIncremented = true, isPrimaryKey = true)
    val versionId: Int

    @SaveToTable
    val fileId: Int

    @SaveToTable
    val width: Int

    @SaveToTable
    val height: Int
}

@Serializable
data class ImageVersion(override val fileId: Int,
                        override val width: Int,
                        override val height: Int): ImageVersionCommon {
    override val versionId: Int = -1
    companion object {
        val sizes = mutableListOf(
            500 x 500 put ScaleType.OUTSIDE,
            400 x 400 put ScaleType.OUTSIDE,
            200 x 200 put ScaleType.OUTSIDE,
            100 x 100 put ScaleType.OUTSIDE)
    }
}

@Serializable
data class StaticImageVersion(override val fileId: Int,
                              override val width: Int,
                              override val height: Int): ImageVersionCommon {
    override val versionId: Int = -1
}