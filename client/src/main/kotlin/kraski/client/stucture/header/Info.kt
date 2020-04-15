package kraski.client.stucture.header

import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import kotlinx.html.ATarget
import kotlinx.html.AreaTarget
import kotlinx.html.SPAN
import kotlinx.serialization.list
import kraski.client.MainStyles
import kraski.client.gridArea
import kraski.client.redKraski
import kraski.client.send
import kraski.common.Contact
import kraski.common.Request
import kraski.common.getPluralForm
import kraski.common.interpretation.DirRef
import kraski.common.interpretation.ImageDirs
import kraski.common.interpretation.Images
import kraski.common.interpretation.JsonRef
import react.RBuilder
import react.dom.RDOMBuilder
import react.dom.img
import styled.*

inline fun RBuilder.yellowSpan(content: String, block: StyledDOMBuilder<SPAN>.() -> Unit = {}) = styledSpan {
    css {
        +MainStyles.yellowString
    }
    block()
    +content
}

fun RBuilder.infoBlock(participantsAmount: Int, daysLeft: Int, contacts: List<Contact>) {

    styledDiv {
        css {
            gridArea = "info"
            display = Display.flex
            justifyContent = JustifyContent.spaceBetween
            alignItems = Align.center
        }
        styledDiv {
            css {
                display = Display.flex
            }
            styledDiv {
                (participantsAmount+500).toString().forEach {
                    yellowSpan(it.toString())
                }
                yellowSpan(participantsAmount.getPluralForm("участник", "участника", "участников").toUpperCase()) {
                    css.marginRight = 40.px
                }
            }
            styledDiv {
                daysLeft.toString().forEach {
                    yellowSpan(it.toString())
                }
                yellowSpan(daysLeft.getPluralForm("день до финала", "дня до финала", "дней до финала").toUpperCase())
            }
        }
        styledDiv {
            //TODO social networks
            contacts.forEach {
                styledA(href = it.link, target = ATarget.blank) {
                    styledImg(src = (ImageDirs.contacts file it.logo).path) {
                        css {
                            width = 25.px
                            marginRight = 10.px
                        }
                    }
                }
            }
            styledA(href = "") {
                css {
                    color = redKraski
                    textDecoration = TextDecoration.none
                    fontWeight = FontWeight.bold
                    display = Display.inlineFlex
                    alignItems = Align.center
                }
                styledImg(src = "/images/contacts/ask.png", alt = "?") {
                    css {
                        width = 25.px
                        marginRight = 10.px
                        borderRadius = 3.px
                    }
                }
                +"Задать вопрос"
            }
        }
    }
}