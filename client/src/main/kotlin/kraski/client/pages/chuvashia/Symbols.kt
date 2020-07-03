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
import react.dom.InnerHTML
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
//                    css {
//                        borderTop(1.px, BorderStyle.solid, gray50Color)
//                    }
                    if (it.header != "") styledH3 {
                        css {
                            fontSize = 24.px
                            padding(0.px, 20.px)
                            textAlign = TextAlign.center
                        }
                        +it.header
                    }
                    styledP {
                        css {
                            overflow = Overflow.hidden
                        }
                        if (it.picture != "hymn.png") {
                            styledImg(src = (ImageDirs.chuvashia / it.picture).path) {
                                css {
                                    padding(20.px)
                                    width = 400.px
                                    float = Float.right
                                }
                            }
                            styledSpan {
                                attrs["dangerouslySetInnerHTML"] = InnerHTML(it.text)
                            }
                        } else {
                            styledP {
                                attrs["dangerouslySetInnerHTML"] = InnerHTML(it.text)
                            }
                            styledImg(src = (ImageDirs.chuvashia / it.picture).path) {
                                css {
                                    width = 600.px
                                    margin(20.px)
                                    float = Float.left
                                }
                            }
                            it.songs.forEach { song ->
                                styledP {
                                    css {
                                        float = Float.left
                                        margin(20.px)
                                    }
                                    attrs["dangerouslySetInnerHTML"] = InnerHTML(song.song)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}