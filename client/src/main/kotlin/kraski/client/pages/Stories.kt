package kraski.client.pages

import kraski.client.*
import kraski.client.pages.join.Contest
import kraski.client.stucture.PageProps
import kraski.client.stucture.PageState
import kraski.client.stucture.StandardPageComponent
import kraski.common.AnswerType
import kraski.common.Request
import kraski.common.models.StoriesWithSrc
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

interface StoriesState : PageState {
    var stories: List<StoriesWithSrc>
    var isAdmin: Boolean
    var editId: Int?
}

class StoriesComponent(props: PageProps) : StandardPageComponent<StoriesState>(props) {
    init {
        state.stories = listOf()
        state.isAdmin = false
        Request.StoriesGetAll(400, 0).send(StoriesWithSrc.serializer().list) {
            setState {
                stories = it
            }
        }
        GlobalScope.launch {
            val r = Request.AdminCheck().send().parseAnswer().answerType == AnswerType.OK
            setState {
                isAdmin = r
            }
        }
    }

    private fun RBuilder.editButton(stories: StoriesWithSrc) {
        styledSpan {
            css {
                marginLeft = 10.px
                color = gray50Color
                textDecoration(TextDecorationLine.underline)
                fontStyle = FontStyle.italic
                cursor = Cursor.pointer
            }
            attrs.onClickFunction = {
                setState { editId = stories.stories.id }
            }
            +"редактировать"
        }
    }

    override fun StyledDOMBuilder<*>.page() {
        state.stories.forEach { stories ->
            styledDiv {
                css {
                    margin(50.px, 0.px)
                    overflow = Overflow.hidden
                    width = 90.pct
                }
                styledH3 {
                    css { color = redKraski }
                    +stories.stories.header
                    if (state.isAdmin) editButton(stories)
                }
                styledP {
                    css {
                        color = gray50Color
                    }
                    if (stories.stories.author.isNotEmpty()) {
                        +stories.stories.author
                    }
                }
                styledDiv {
                    css { margin(10.px, 0.px) }
                    styledDiv {
                        css { margin(0.px, 10.px)}
                        attrs["dangerouslySetInnerHTML"] = InnerHTML(stories.stories.shortContent)
                    }
                    styledDiv {
                        css { margin(10.px, 10.px, 10.px, 10.px)}
                        attrs["dangerouslySetInnerHTML"] = InnerHTML(stories.stories.content)
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
                                formType = FormType.StoriesForm
                            }
                        }
                    }
                }
            }
        }
    }
}
