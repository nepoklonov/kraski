package kraski.client.pages

import kotlinx.css.*
import kotlinx.serialization.list
import kraski.client.gray50Color
import react.dom.a
import kraski.client.gray70Color
import kraski.client.gridArea
import kraski.client.send
import kraski.client.stucture.*
import kraski.common.Request
import kraski.common.interpretation.JsonRef
import kraski.common.Contact
import kraski.common.interpretation.ImageDirs
import styled.*

class ContactsComponent(props: PageProps) : StandardPageComponent<YamlListState<Contact>>(props) {
    init {
        initYamlListState()
        Request.GetJson(JsonRef.ContactsJson).send(Contact.serializer().list, ::updateYamlListState)
    }

    override fun StyledDOMBuilder<*>.page() {
        styledDiv {
            css {
                display = Display.flex
                justifyContent = JustifyContent.spaceBetween
                flexWrap = FlexWrap.wrap
            }
            state.yaml.forEach {
                styledDiv {
                    css {
                        margin(0.px, 15.px, 15.px, 0.px)
                        display = Display.flex
                        flexDirection = FlexDirection.column
                        alignItems = Align.center
                    }
                    a(href = it.link, target = "_blank") {
                        styledImg(src = (ImageDirs.contacts file it.logo).path) {
                            css {
                                width = 60.px
                                height = 60.px
                            }
                        }
                    }
                    styledSpan {
                        css {
                            fontSize = 13.px
                            color = gray50Color
                        }
                        +it.text
                    }
                }
            }
        }
    }
}