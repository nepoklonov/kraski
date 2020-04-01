package kraski.server

import kraski.common.interpretation.PlanarSize
import kraski.common.interpretation.Scale
import kraski.common.interpretation.x
import java.awt.RenderingHints
import java.awt.image.BufferedImage

inline fun getScaledImage(src: BufferedImage, scale: Scale
                          , transform: (PlanarSize, PlanarSize) -> PlanarSize
                          = { _, rectSize -> rectSize }): BufferedImage {
    val size = transform(src.width x src.height, scale.size)
    val result = BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB)
    val g2 = result.createGraphics()
//    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC)
    g2.setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY )
    g2.drawImage(src, 0, 0, size.width, size.height, null)
    g2.dispose()
    return result
}

fun scaleImageByRect(src: BufferedImage, scale: Scale) =
    getScaledImage(src, scale) { srcSize, _ ->
        calculateSizeByScale(srcSize, scale)
    }

fun calculateSizeByScale(srcSize: PlanarSize, scale: Scale): PlanarSize {
    val factor = srcSize.width.toDouble() / srcSize.height.toDouble()
    var finalWidth = scale.size.width
    var finalHeight = scale.size.height
    if (scale.type.flag xor (srcSize.run { width / height } > scale.size.run { width / height })) {
        finalHeight = (scale.size.width / factor).toInt()
    } else {
        finalWidth = (scale.size.height * factor).toInt()
    }
    return finalWidth x finalHeight
}

//fun rotateClockwise90(src: BufferedImage): BufferedImage {
//    val width = src.width
//    val height = src.height
//
//    val result = BufferedImage(height, width, src.type)
//
//    val graphics2D = result.createGraphics()
//    graphics2D.translate((height - width) / 2, (height - width) / 2)
//    graphics2D.rotate(Math.PI / 2, (height / 2).toDouble(), (width / 2).toDouble())
//    graphics2D.drawRenderedImage(src, null)
//
//    return result
//}

//private fun cropImage(src: BufferedImage, w: Int, h: Int): BufferedImage {
//    val width = src.width.toDouble()
//    val height = src.height.toDouble()
//
//    return when {
//        w / width < h / height -> {
//            val finalW = ((height * w) / h).toInt()
//            val finalH = height.toInt()
//
//            val x = ((width - finalW) / 2).toInt()
//            val y = 0
//            src.getSubimage(x, y, finalW, finalH)
//        }
//        w / width > h / height -> {
//            val finalW = width.toInt()
//            val finalH = ((width * h) / w).toInt()
//
//            val x = 0
//            val y = ((height - finalH) / 2).toInt()
//            src.getSubimage(x, y, finalW, finalH)
//        }
//        else -> src
//    }
//}