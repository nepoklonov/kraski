package kraski.client.pages

import kraski.client.*
import kraski.client.elements.image.imageInDiv
import kraski.client.stucture.PageComponent
import kraski.client.stucture.PageProps
import kraski.client.stucture.PageState
import kraski.common.Request
import kraski.common.interpretation.ImageDirs
import kraski.common.interpretation.Pages
import kraski.common.models.NewsWithSrc
import kotlinx.css.*
import kotlinx.css.LinearDimension.Companion.auto
import kotlinx.css.properties.deg
import kotlinx.css.properties.rotate
import kotlinx.html.ATarget
import kotlinx.html.js.onClickFunction
import kotlinx.serialization.list
import kraski.common.General
import kraski.common.quote
import org.w3c.dom.HTMLElement
import org.w3c.dom.get
import react.RBuilder
import react.dom.InnerHTML
import react.dom.a
import react.dom.br
import react.dom.p
import react.setState
import styled.*
import kotlin.browser.document
import kotlin.browser.window
import kotlin.math.max
import kotlin.math.min

fun RBuilder.yellowLine(width: LinearDimension = 100.pct) = styledDiv {
    css {
        background = "url(/images/design/three-up-yellow.png) round"
        backgroundSize = "14px"
        height = 10.px
        this.width = width
    }
}

fun RBuilder.news(title: String, subtitle: String, content: String, imageSrc: String) {
    styledDiv {
        css {
            display = Display.grid
            gridTemplateAreas("image title", "image subtitle", "image imageContent")
            gridTemplateRows = GridTemplateRows(auto, 1.fr, auto)
            gridTemplateColumns = GridTemplateColumns(2.fr, 3.fr)
            rowGap = RowGap(5.px.value)
            columnGap = ColumnGap(20.px.value)
            //height = 100.px
            margin(15.px, 0.px)
        }
        imageInDiv(imageSrc, "contain", width = 200.px) {
            css {
                gridArea = "image"
            }
        }
        styledDiv {
            css {
                fontSize = 18.px
                gridArea = "title"
                fontWeight = FontWeight.bold
            }
            +title
        }
        styledDiv {
            css {
                gridArea = "subtitle"
                color = gray70Color
            }
            +subtitle
        }
        styledDiv {
            css {
                overflow = Overflow.hidden
                gridArea = "imageContent"
            }
            attrs["dangerouslySetInnerHTML"] = InnerHTML(content.match("^(([^.?!])|(\\S[.?!]\\S))*[.?!]*")?.get(0)
                ?: "")
        }
    }
}

interface MainState : PageState {
    var news: List<NewsWithSrc>
    var index: Int
}

class MainComponent(props: PageProps) : PageComponent<MainState>(props) {
    private val amount = 5

    init {
        document.title = "Земля Калевалы"
        state.index = 0
        state.news = listOf()
        Request.NewsGetAll(600, 0).send(NewsWithSrc.serializer().list) {
            setState {
                news = it
            }
        }
    }

    override fun StyledDOMBuilder<*>.page() {
        styledDiv {
            styledDiv {
                yellowLine()
                styledDiv {
                    css {
                        display = Display.flex
                        justifyContent = JustifyContent.spaceBetween
                        alignItems = Align.stretch
                        margin(10.px, 0.px)
                        paddingBottom = 10.px
                    }
                    styledImg(src = (ImageDirs.team / "arshinova.jpg").path) {
                        css {
                            height = 140.px
                            borderRadius = 100.pct
                            margin(17.px, 5.px, 0.px, 5.px)
                        }
                    }
                    styledImg(src = (ImageDirs.design / "quote.png").path) {
                        css {
                            height = 40.px
                            margin(17.px, 5.px, 0.px, 5.px)
                        }
                    }
                    styledImg(src = (ImageDirs.design / "quote.png").path) {
                        css {
                            height = 40.px
                            margin(17.px, 5.px, 0.px, 5.px)
                        }
                    }
                    styledDiv {
                        css {
                            flex(1.0, 1.0)
                            display = Display.flex
                            flexWrap = FlexWrap.wrap
                            fontSize = 16.pt
                            margin(12.px, 5.px)
                            justifyContent = JustifyContent.flexEnd
                        }
                        styledP {
                            css {
                                textAlign = TextAlign.left
                                alignSelf = Align.flexEnd
                            }
                            +"Главной целью нашего конкурса является сохранение и популяризация самобытной материальной и духовной культуры Чувашии. Отрадно, что конкурс востребован, с каждым годом количество участников увеличивается, расширяется географический формат проекта."
                        }
                        styledP {
                            css {
                                color = rgb(230, 100, 100)
                                fontSize = 12.pt
                                alignSelf = Align.flexEnd
                            }
                            +"Алёна Игоревна Аршинова,"
                            br {}
                            +"депутат Государственной Думы ФС РФ,"
                            br {}
                            +"куратор Всероссийского фестиваля ${General.ruTitle.quote()}"
                        }
                    }
                }
                yellowLine()
            }
//            styledDiv {
//                css {
//                    display = Display.grid
//                    gridTemplateRows = GridTemplateRows(40.px, auto, 40.px)
//                    rowGap = RowGap(10.px.value)
//                    alignContent = Align.flexStart
//                }
//                styledDiv {
//                    css {
//                        display = Display.grid
//                        rowGap = RowGap(20.px.value)
//                    }
//                    if (state.news != undefined && state.news.isNotEmpty()) {
//                        state.news.forEach {
//                            news(it.news.header, it.news.date, it.news.shortContent, it.src)
//                        }
//                    }
//                }
            styledH2 {
                css {
                    marginTop = 20.px
                }
                +"Наши Новости"
            }
            state.news.forEach { news ->
                styledDiv {
                    css {
                        overflow = Overflow.hidden
                        margin(0.px, 0.px, 50.px, 0.px)
                        width = 90.pct
                    }
                    styledH3 {
                        css { color = redKraski }
                        +news.news.header
                    }
                    styledP {
                        css {
                            color = gray50Color
                        }
                        +news.news.date.split('.').reversed().joinToString(".") { it }
                        if (news.news.author.isNotEmpty()) {
                            +" — "
                            +news.news.author
                        }
                    }
                    styledDiv {
                        css { margin(10.px, 0.px) }
                        if (news.src != "") styledImg(src = news.src) {
                            css {
                                width = 400.px
                                marginLeft = 30.px
                                float = Float.right
                            }
                        }
                        styledDiv {
                            attrs["dangerouslySetInnerHTML"] = InnerHTML(news.news.content)
                        }
                    }
                }
//                }
            }
        }
    }
}