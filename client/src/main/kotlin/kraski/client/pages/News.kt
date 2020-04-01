package kraski.client.pages

import kraski.client.*
import kraski.client.pages.join.Contest
import kraski.client.stucture.PageProps
import kraski.client.stucture.PageState
import kraski.client.stucture.StandardPageComponent
import kraski.common.AnswerType
import kraski.common.Request
import kraski.common.models.NewsWithSrc
import kraski.common.models.participants.FormType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.css.properties.TextDecorationLine
import kotlinx.css.properties.textDecoration
import kotlinx.html.js.onClickFunction
import kotlinx.serialization.list
import react.RBuilder
import react.dom.InnerHTML
import react.setState
import styled.*

interface NewsState : PageState {
    var news: List<NewsWithSrc>
    var isAdmin: Boolean
    var editId: Int?
}

class NewsComponent(props: PageProps) : StandardPageComponent<NewsState>(props) {
    init {
        state.news = listOf()
        state.isAdmin = false
        Request.NewsGetAll(400, 0).send(NewsWithSrc.serializer().list) {
            setState {
                news = it
            }
        }
        GlobalScope.launch {
            val r = Request.AdminCheck().send().parseAnswer().answerType == AnswerType.OK
            setState {
                isAdmin = r
            }
        }
    }

    private fun RBuilder.editButton(news: NewsWithSrc) {
        styledSpan {
            css {
                marginLeft = 10.px
                color = gray50Color
                textDecoration(TextDecorationLine.underline)
                fontStyle = FontStyle.italic
                cursor = Cursor.pointer
            }
            attrs.onClickFunction = {
                setState { editId = news.news.id }
            }
            +"редактировать"
        }
    }

    override fun StyledDOMBuilder<*>.page() {
        state.news.forEach { news ->
            styledDiv {
                css {
                    margin(50.px, 0.px)
                    overflow = Overflow.hidden
                    width = 90.pct
                }
                styledH3 {
                    css { color = redKraski }
                    +news.news.header
                    if (state.isAdmin) editButton(news)
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
                            float = Float.right
                            marginLeft = 30.px
                        }
                    }
                    styledDiv {
                        attrs["dangerouslySetInnerHTML"] = InnerHTML(news.news.content)
                    }
                }
            }
        }

        if (state.editId != null) {
            child(FrameComponent::class) {
                attrs {
                    width = 60.pct
                    height = 80.pct
                    close = {
                        setState {
                            editId = null
                        }
                    }
                    content = {
                        child(Contest::class) {
                            attrs {
                                editId = state.editId
                                formType = FormType.NewsForm
                            }
                        }
                    }
                }
            }
        }
    }
}
