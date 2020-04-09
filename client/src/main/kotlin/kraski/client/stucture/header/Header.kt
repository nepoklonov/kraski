package kraski.client.stucture.header

import kotlinext.js.assign
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.css.Display
import kotlinx.css.properties.TextDecoration
import kotlinx.css.properties.borderBottom
import kotlinx.serialization.ContextSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.list
import kraski.client.*
import kraski.client.stucture.GalleryPreview
import kraski.common.*
import kraski.common.interpretation.ImageDirs
import kraski.common.interpretation.JsonRef
import react.*
import react.dom.a
import react.dom.br
import react.router.dom.navLink
import styled.*
import kotlin.browser.window
import kotlin.js.Date
import kotlin.reflect.KMutableProperty1

val DAY_OF_THE_END = Date.UTC(2020, 4, 28).toLong()
val DAY_OF_THE_STOP = Date.UTC(2020, 1, 20).toLong()
const val msInDay = 1000 * 3600 * 24

fun getDaysLeft() = (DAY_OF_THE_END / msInDay - Date.now().toLong() / msInDay).toInt()
//fun getAccessDaysLeft() = (DAY_OF_THE_STOP / msInDay - Date.now().toLong() / msInDay).toInt()

interface HeaderState : RState {
    var generalInfo: GeneralInfo
    var contacts: List<Contact>
    var partners: List<Partner>
}

inline fun <T, S : RState> RComponent<*, S>.initState(prop: KMutableProperty1<S, T>, default: T, crossinline valueFunction: suspend () -> T) {
    prop.set(state, default)
    GlobalScope.launch {
        val newT = valueFunction()
        setState {
            prop.set(this, newT)
        }
    }
}

inline fun <reified T : Any, S : RState, reified R : Request> RComponent<*, S>.serverState(prop: KMutableProperty1<S, T>, default: T, request: R, serializer: KSerializer<T> = T::class.getSerializer()) {
    prop.set(state, default)
    GlobalScope.launch {
        val newT = request.send().parseAnswerBody(serializer)
        setState {
            prop.set(this, newT)
        }
    }
}

class HeaderComponent : RComponent<RoutedProps, HeaderState>() {
    init {
        serverState(HeaderState::generalInfo, GeneralInfo(), Request.GetGeneralInfo())
        serverState(HeaderState::contacts, listOf(), Request.GetJson(JsonRef.ContactsJson), Contact.serializer().list)
        serverState(HeaderState::partners, listOf(), Request.GetJson(JsonRef.LogosJson), Partner.serializer().list)
    }

    override fun RBuilder.render() {
        infoBlock(state.generalInfo.participantsAmount, getDaysLeft(), state.contacts)
        styledDiv {
            css {
                backgroundImage = Image("url(/images/design/top-back.jpg)")
                height = 140.px
                backgroundSize = "cover"
                backgroundPosition = "center"
                gridArea = "h-back"
            }
        }
        styledDiv {
            css {
                gridArea = "ornament"
                width = 140.px
                height = 100.pct
                backgroundSize = "50%"
                backgroundImage = Image("url(/images/design/background.png)")
                justifySelf = JustifyContent.center
            }
        }
        styledDiv {
            css {
                gridArea = "header"
                display = Display.flex
                justifyContent = JustifyContent.spaceBetween
                alignItems = Align.center
            }
            styledDiv {
                css {
                    color = redKraski
                    display = Display.flex
                    flexDirection = FlexDirection.column
                }
                styledH1 {
                    +General.ruTitle
                }
                styledDiv {
                    css {
                        width = 100.pct
                        height = 10.px
                        background = "url(/images/design/three-up.png) round"
                        backgroundSize = "14px"
                    }
                }
                styledH2 {
                    +General.shortDescription
                }
            }
            child(GalleryPreview::class) {}
            styledDiv {
                css {
                    width = 70.px
                    height = 100.pct
                    backgroundSize = "100%"
                    backgroundImage = Image("url(/images/design/background.png)")
                }
            }
        }
        styledDiv {
            css {
                gridArea = "head-nav"
                display = Display.flex
                alignItems = Align.center
            }
            styledUl {
                listOf(Section.Gallery, Section.Team, Section.Partners, Section.Contacts).forEach {
                    styledLi {
                        css {
                            display = Display.inline
                            textAlign = TextAlign.center
                            paddingBottom = 3.px
                            marginRight = 25.px
                            borderBottom(3.px, BorderStyle.solid, Color.white)
                            hover {
                                borderBottom(3.px, BorderStyle.solid, redKraskiWithOpacity(0.3))
                            }
                            active {
                                borderBottom(3.px, BorderStyle.solid, redKraski)
                            }
                            child("a") {
                                color = redKraski
                                textDecoration = TextDecoration.none
                                fontWeight = FontWeight.bold
                            }
                        }
                        styledImg(src = "images/design/three-down.png") {
                            css {
                                height = 12.px
                                marginRight = 3.px
                            }
                        }
                        navLink(to = it.url) {
                            +it.title
                        }
                    }
                }
            }
        }
        styledDiv {
            css {
                gridArea = "left-nav"
            }
            styledUl {
                css {
                    child("a") {
                        textDecoration = TextDecoration.none
                    }
                    child("a li") {
                        display = Display.flex
                        justifyContent = JustifyContent.center
                        alignItems = Align.center
                        textAlign = TextAlign.center
                        margin(10.px, 0.px)
                        padding(10.px, 0.px)
                        cursor = Cursor.pointer
                    }
                }
                navLink(to = Section.Main.url) {
                    styledLi {
                        styledImg(src = "/images/design/logo-kraski.png") {
                            css.width = 100.pct
                        }
                    }
                }
                listOf(Section.Join).forEach {
                    navLink(to = it.url) {
                        styledLi {
                            css {
                                backgroundColor = redKraskiWithOpacity(0.8)
                                hover {
                                    backgroundColor = redKraskiWithOpacity(0.9)
                                }
                                active {
                                    backgroundColor = redKraski
                                    padding(11.px, 0.px, 9.px, 0.px)
                                }
                                fontWeight = FontWeight.bold
                                textDecoration = TextDecoration.none
                                color = lightYellow
                            }
                            +it.title
                        }
                    }
                }
                listOf(Section.About, Section.News, Section.Stories, Section.Raskraska, Section.History,
                    Section.Chuvashia).forEach {
                    navLink(to = it.url) {
                        styledLi {
                            css {
                                backgroundColor = lightYellow
                                hover {
                                    backgroundColor = mediumYellow
                                }
                                active {
                                    backgroundColor = hardYellow
                                    padding(11.px, 0.px, 9.px, 0.px)
                                }
                                fontWeight = FontWeight.bold
                                textDecoration = TextDecoration.none
                                color = redKraski
                            }
                            +it.title
                        }
                    }
                }
                state.partners.forEach {
                    a(href = it.link) {
                        styledLi {
                            css {
                                backgroundColor = lightYellow
                                hover {
                                    backgroundColor = mediumYellow
                                }
                                active {
                                    backgroundColor = hardYellow
                                    padding(11.px, 0.px, 9.px, 0.px)
                                }
                            }
                            styledImg(src = (ImageDirs.logos / it.logo).path) {
                                css {
                                    width = 70.pct
                                }
                            }
                        }
                    }
                }
            }
        }

        styledDiv {
            css {
                backgroundColor = lightYellow
            }
            styledDiv {
                css {
                    width = 100.pct
                    height = 10.px
                    background = "url(/images/design/three-up.png) round"
                    backgroundSize = 14.px.toString()
                }
            }
            css {
                gridArea = "footer"
            }
            styledDiv {
                css {
                    padding(20.px, 5.pct)
                    fontWeight = FontWeight.bold
                }
                +"© ${General.ruTitle.quote()}, 2019 — 2020"
                br {}
                +"Полное или частичное копирование материалов запрещено, при согласованном копировании ссылка на ресурс обязательна."
            }
        }
    }

    override fun componentDidMount() {
        window.addEventListener("resize", { forceUpdate() })
    }

    override fun componentWillUnmount() {
        window.removeEventListener("resize", { forceUpdate() })
    }
}