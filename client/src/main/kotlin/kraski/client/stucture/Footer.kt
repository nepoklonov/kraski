package kraski.client.stucture

import kraski.client.pages.logos
import kraski.client.send
import kraski.common.*
import kraski.common.interpretation.ImageDirs
import kraski.common.interpretation.JsonRef
import kotlinx.css.*
import kotlinx.css.Display
import kotlinx.css.properties.borderTop
import kraski.common.General
import kraski.common.Partners
import kraski.common.Request
import react.RBuilder
import react.RComponent
import react.RProps
import react.dom.a
import react.dom.span
import styled.*

class FooterComponent : RComponent<RProps, YamlState<Partners>>() {
    init {
        Request.GetJson(JsonRef.PartnersJson).send(Partners.serializer(), ::updateYamlState)
    }
    override fun RBuilder.render() {
        styledDiv {
            css {
                width = 100.pct
                padding(80.px, 80.px, 10.px, 80.px)
                display = Display.flex
                flexDirection = FlexDirection.column
                alignItems = Align.center
                backgroundColor = rgba(255, 255, 255, 0.7)
                fontSize = 18.px
            }
            styledDiv {
                css {
                    borderTop(2.px, BorderStyle.solid, Color.black)
                    width = 100.pct
                    alignSelf = Align.flexStart
                }
                styledP {
                    css {
                        fontWeight = FontWeight.bold
                        fontSize = 22.pt
                        marginTop = 60.px
                    }
                    +"Контакты:".toUpperCase()
                }
                styledDiv {
                    css {
                        display = Display.flex
                        justifyContent = JustifyContent.spaceBetween
                    }
                    styledDiv {
                        css {
                            display = Display.flex
                            alignItems = Align.flexEnd
                        }
                        span {
                            styledSpan {
                                css {
                                    fontWeight = FontWeight.bold
                                }
                                +"E-mail:\t ".toUpperCase()
                            }
                            span {
                                +General.eMail
                            }
                        }
                        fun contactImg(href: String, filename: String) = a(href = href) {
                            styledImg(src = (ImageDirs.contacts file filename).path) {
                                css {
                                    margin(0.px, 0.px, 0.px, 30.px)
                                    height = 42.px
                                }
                            }
                        }
                        contactImg("https://www.instagram.com/kalevala_fest/", "inst.png")
                        contactImg("https://vk.com/kalevala_fest/", "vk.png")
                        contactImg("https://www.facebook.com/kalevfest/", "fb.png")
                    }
                    styledDiv {
                        if (state.yaml != undefined) {
//                            logos(null, state.yaml.orgs, 42.px, 0.px)
                        }
                    }
                }
            }

            styledSpan {
                css {
                    fontWeight = FontWeight.bold
                    padding(20.px, 0.px)
                    textAlign = TextAlign.center
                }
                +"© ${General.ruTitle.quote()}, 2019. Полное или частичное копирование материалов сайта запрещено"
                +", при согласованном копировании ссылка на ресурс обязательна."
            }
        }
    }
}