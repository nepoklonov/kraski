package kraski.client.stucture.header

import kraski.common.interpretation.Pages
import kraski.common.quote
import kotlinx.css.*
import kotlinx.html.DIV
import react.router.dom.navLink
import styled.*

val drawCommonwealth: StyledDOMBuilder<DIV>.() -> Unit = {
    styledDiv {
        css {
            fontSize = 16.px
            width = 100.pct
            height = 100.pct
            position = Position.relative
        }
        styledDiv {
            css {
                width = 20.pct
                height = 100.pct
                position = Position.absolute
                display = Display.flex
                flexDirection = FlexDirection.column
                justifyContent = JustifyContent.stretch
            }
            fun r(color: Color) = styledDiv {
                css {
                    flex(1.0)
                    background = "linear-gradient(90deg, $color, transparent)"
                }
            }

            r(rgb(238, 134, 141))
            r(rgb(128, 189, 222))
            r(rgb(131, 196, 160))
        }
        styledDiv {
            css {
                display = Display.flex
                justifyContent = JustifyContent.center
                flexWrap = FlexWrap.wrap
                position = Position.relative
                textAlign = TextAlign.center
                zIndex = 5
                padding(20.px, 10.px)
            }
            styledP {
                +"МОО "
                +"Карельское Содружество".quote()
                +" является добровольным самоуправляемым общественным объединением, созданным на основе свободного волеизъявления физических лиц, объединившихся на основе общности интересов и убеждений по развитию культурных отношений."
                css {
                    marginBottom = 20.px
                }
            }
            navLink(Pages.commonwealth.path) {
                styledButton {
                    css {
                        display = Display.block
                        backgroundColor = Color.transparent
                        padding(5.px)
                        cursor = Cursor.pointer
                    }
                    +"страница организации"
                }
            }
        }
    }
}