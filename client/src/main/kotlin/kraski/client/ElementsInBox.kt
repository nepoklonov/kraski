package kraski.client

import kotlinx.css.*
import kotlinx.html.DIV
import kotlinx.html.classes
import react.RBuilder
import styled.*

inline fun RBuilder.elementInBox(x1: Int, y1: Int, x2: Int, y2: Int, element: StyledDOMBuilder<DIV>.() -> Unit) {
    styledDiv {
        css {
            display = Display.flex
            justifyContent = JustifyContent.center
            alignItems = Align.center
            position = Position.absolute
            left = (scale * x1).px
            top = (scale * y1).px
            width = (scale * (x2 - x1)).px
            height = (scale * (y2 - y1)).px
        }
        element()
    }
}

inline fun RBuilder.labelInBox(text: String, x1: Int, y1: Int, x2: Int, y2: Int, classes: Set<String> = setOf(""), userStyle: RuleSet = {}) {
    elementInBox(x1, y1, x2, y2) {
        styledSpan {
            attrs.classes = classes
            +text
            css {
                userStyle()
            }
        }
    }
}

inline fun RBuilder.imageInBox(src: String, x1: Int, y1: Int, x2: Int, y2: Int, classes: Set<String> = setOf(""), userStyle: RuleSet = {}) {
    elementInBox(x1, y1, x2, y2) {
        styledImg(src = src) {
            attrs.classes = classes
            css {
                maxWidth = 100.pct
                maxHeight = 100.pct
                userStyle()
            }
        }
    }
}