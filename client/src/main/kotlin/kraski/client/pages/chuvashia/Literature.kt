package kraski.client.pages.chuvashia

import kotlinx.css.*
import kotlinx.css.properties.borderTop
import kraski.client.send
import kraski.client.stucture.*
import kraski.common.Request
import kraski.common.interpretation.JsonRef
import kotlinx.serialization.list
import kraski.client.gray50Color
import kraski.common.LiteratureJSON
import react.dom.a
import react.dom.br
import styled.*

class Literature(props: PageProps) : StandardPageComponent<YamlListState<LiteratureJSON>>(props) {
    init {
        initYamlListState()
        Request.GetJson(JsonRef.ChuvashiaLiteratureJson).send(LiteratureJSON.serializer().list, ::updateYamlListState)
    }

    override fun StyledDOMBuilder<*>.page() {
        styledP {
            css {
                margin(5.px)
            }
            +"0 — материалы неограниченного сетевого доступа (материалы пользовательского фонда, "
            +"доступные через глобальную сеть). Например, "
            a(href = "http://elbib.nbchr.ru/lib_files/0/ichk_0_0000150.pdf") {
                +"http://elbib.nbchr.ru/lib_files/0/ichk_"
                styledSpan {
                    css { backgroundColor = Color.pink }
                    +"0"
                }
                +"_0000150.pdf"
            }
            br {}
            +"1 — материалы ограниченного сетевого доступа (материалы пользовательского фонда, "
            +"доступные по локальным или корпоративным сетям). Например, "
            a(href = "http://elbibw.nbchr.ru/ellib/kkni/kkni_1_0001200.pdf") {
                +"http://elbibw.nbchr.ru/ellib/kkni/kkni_"
                styledSpan {
                    css { backgroundColor = Color.pink }
                    +"1"
                }
                +"_0001200.pdf"
            }
        }

        state.yaml.forEach {
            styledDiv {
                css {
                    borderTop(1.px, BorderStyle.solid, gray50Color)
                }
                if (it.header != "") styledH3 {
                    css {
                        fontSize = 20.px
                        position = Position.relative
                        top = (-14).px
                        backgroundColor = Color.white
                        padding(0.px, 20.px)
                        textAlign = TextAlign.center
                    }
                    +it.header
                }
                styledH4 {
                    css {
                        textAlign = TextAlign.center
                        fontSize = 16.px
                        margin(10.px)
                    }
                    +it.subheader
                }
                it.content.forEach {
                    styledP {
                        css {
                            margin(5.px)
                            padding(0.px, 20.px)
                            "first-letter" {
                                marginLeft = 30.px
                            }
                        }
                        styledB {
                            +(it.bold + " ")
                        }
                        styledSpan {
                            +(it.light + " ")
                        }
                        styledA(href = it.link) { +it.link }
                    }
                }
            }
        }
    }
}