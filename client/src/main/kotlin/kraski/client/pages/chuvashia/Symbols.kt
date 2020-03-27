package kraski.client.pages.chuvashia

import kraski.client.send
import kraski.client.stucture.*
import kraski.common.SymbolJSON
import kraski.common.Request
import kraski.common.interpretation.ImageDirs
import kraski.common.interpretation.JsonRef
import kotlinx.css.*
import kotlinx.css.properties.borderTop
import kotlinx.serialization.list
import kraski.client.gray50Color
import styled.*

class Symbols(pageProps: PageProps) : StandardPageComponent<YamlListState<SymbolJSON>>(pageProps) {
    init {
        initYamlListState()
        Request.GetJson(JsonRef.ChuvashiaSymbolsJson).send(SymbolJSON.serializer().list, ::updateYamlListState)
    }

    override fun StyledDOMBuilder<*>.page() {
        if (state.yaml != undefined) {
            state.yaml.forEach {
                styledDiv {
                    css {
                        borderTop(1.px, BorderStyle.solid, gray50Color)
                    }
                    if (it.header != "") styledH3 {
                        css {
                            fontSize = 16.px
                            position = Position.relative
                            top = (-14).px
                            backgroundColor = Color.white
                            padding(0.px, 20.px)
                            textAlign = TextAlign.center
                        }
                        +it.header
                    }
                    styledP {
                        css {
                            display = Display.flex
                            justifyContent = JustifyContent.center
                        }
                        +it.text
                        styledImg(src = (ImageDirs.chuvashia / it.picture).path) {
                            css {
                                margin(20.px)
                                width = 400.px
                            }
                        }
                    }
                    it.songs.forEach { song ->
                        +song.song
                    }
                }
            }
        }
    }
}