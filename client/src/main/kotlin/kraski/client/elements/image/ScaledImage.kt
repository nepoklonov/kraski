package kraski.client.elements.image

import kraski.client.parseAnswerBody
import kraski.client.send
import kraski.common.Request
import kraski.common.interpretation.*
import kraski.common.interpretation.Image
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.DIV
import kotlinx.serialization.serializer
import react.*
import styled.StyledDOMBuilder
import styled.css
import styled.styledDiv


interface ScaleImageProps : RProps {
    var image: Image<ScaleContainer>
    var scale: Scale
    var block: StyledDOMBuilder<DIV>.() -> Unit
}

interface ScaledImageState : RState {
    var src: String
}

inline fun RBuilder.imageInDiv(src: String, backgroundSize: String, width: LinearDimension = LinearDimension.auto, height: LinearDimension = LinearDimension.auto, block: StyledDOMBuilder<DIV>.() -> Unit = {}) {
    styledDiv {
        css {
            backgroundImage = kotlinx.css.Image("url('${src}')")
            backgroundRepeat = BackgroundRepeat.noRepeat
            this.backgroundSize = backgroundSize
            backgroundPosition = "center center"
            this.width = width
            this.height = height
        }
        block()
    }
}

class ScaledImage : RComponent<ScaleImageProps, ScaledImageState>() {
    init {
        GlobalScope.launch {
            val srcFromServer = Request.StaticImagesGetVersion(props.image.original, props.scale)
                .send().parseAnswerBody(String.serializer())
            setState { src = srcFromServer }
        }
    }

    private fun Int.pxOrAuto() = when (this) {
        0, Int.MAX_VALUE, Int.MIN_VALUE -> LinearDimension.auto
        else -> this.px
    }

    override fun RBuilder.render() {
        if (state.src != undefined) {
            val backgroundSize = if (props.scale.type == ScaleType.OUTSIDE) "cover" else "contain"
            val width = props.scale.size.width.pxOrAuto()
            val height = props.scale.size.height.pxOrAuto()
            imageInDiv(state.src, backgroundSize, width, height) {
                props.run { block() }
            }
        }
    }
}

fun <T : ScaleContainer> RBuilder.scaledImage(image: Image<T>, scale: Scale, block: StyledDOMBuilder<DIV>.() -> Unit = { }) {
    child(ScaledImage::class) {
        attrs.image = image
        attrs.scale = scale
        attrs.block = block
    }
}

fun <T : ScaleContainer> RBuilder.scaledImage(image: Image<T>, scale: T, block: StyledDOMBuilder<DIV>.() -> Unit = { }) = scaledImage(image, scale.scale, block)

fun RBuilder.scaledImage(image: Image<SingleScale>, block: StyledDOMBuilder<DIV>.() -> Unit = { }) = scaledImage(image, image.scales[0], block)

fun RBuilder.svgImage(image: SVGImage, size: PlanarSize, block: StyledDOMBuilder<DIV>.() -> Unit = { }) = imageInDiv(image.fileRef.path, "contain", size.width.px, size.height.px) {
    block()
}