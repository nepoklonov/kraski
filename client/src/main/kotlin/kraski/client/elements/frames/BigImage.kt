package kraski.client.elements.frames

import kraski.client.FrameComponent
import kraski.client.FrameProps
import kotlinx.css.*
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import styled.css
import styled.styledDiv

interface BigImageProps : RProps {
    var close: () -> Unit
    var src: String
    var infoBlock: RBuilder.() -> Unit
}

interface BigImageState : RState

class BigImage : RComponent<BigImageProps, BigImageState>() {
    private val imageContent = fun RBuilder.() {
        styledDiv {
            css {
                backgroundColor = rgba(0, 0, 0, 0.8)
                backgroundImage = Image("url('${props.src}')")
                backgroundRepeat = BackgroundRepeat.noRepeat
                backgroundSize = "contain"
                backgroundPosition = "center center"
                width = 60.vw
                height = 100.pct
            }
        }
        (props.infoBlock)()

    }

    override fun RBuilder.render() {
        child<FrameProps, FrameComponent> {
            attrs.close = props.close
            attrs.content = imageContent
            attrs.width = LinearDimension.auto
            attrs.height = 90.pct
        }
    }

}
