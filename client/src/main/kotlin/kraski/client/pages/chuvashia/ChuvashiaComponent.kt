package kraski.client.pages.chuvashia

import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import kraski.client.stucture.PageProps
import kraski.client.stucture.PageState
import kraski.client.stucture.StandardPageComponent
import kotlinx.serialization.list
import kraski.client.redKraski
import kraski.client.stucture.header.serverState
import kraski.common.ChuvashiaSection
import kraski.common.Request
import kraski.common.interpretation.ImageDirs
import kraski.common.interpretation.JsonRef
import kraski.common.interpretation.Pages
import react.router.dom.navLink
import styled.*

interface ChuvashiaState : PageState {
    var sections: List<ChuvashiaSection>
}

class ChuvashiaComponent(pageProps: PageProps) : StandardPageComponent<ChuvashiaState>(pageProps) {
    init {
        serverState(ChuvashiaState::sections, listOf(),
                Request.GetJson(JsonRef.ChuvashiaSectionsJson), ChuvashiaSection.serializer().list)
    }

    override fun StyledDOMBuilder<*>.page() {
        styledDiv {
            css {
                display = Display.flex
                justifyContent = JustifyContent.spaceAround
                flexWrap = FlexWrap.wrap
                margin(30.px, 0.px)
            }
            state.sections.forEach {
                styledDiv {
                    css {
                        width = 250.px
                        display = Display.flex
                        flexWrap = FlexWrap.wrap
                        justifyContent = JustifyContent.center
                        alignItems = Align.center
                        alignContent = Align.center
                        textAlign = TextAlign.center
                        marginBottom = 50.px
                    }
                    navLink((Pages.chuvashia / it.name).path) {
                        styledImg(src = (ImageDirs.chuvashia / it.logo).path) {
                            css {
                                width = 100.px
                                height = 100.px
                                marginBottom = 10.px
                                cursor = Cursor.pointer
                            }
                        }
                        styledSpan {
                            css {
                                fontSize = 18.px
                                fontWeight = FontWeight.bold
                                width = 100.pct
                                color = redKraski
                                textDecoration = TextDecoration.none
                                display = Display.block
                            }
                            +it.title
                        }
                    }
                }
            }
        }
    }
}
