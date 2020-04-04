package kraski.client.pages

import kraski.client.*
import kraski.client.pages.join.Contest
import kraski.client.stucture.PageProps
import kraski.client.stucture.PageState
import kraski.client.stucture.StandardPageComponent
import kraski.common.AnswerType
import kraski.common.Request
import kraski.common.models.RaskraskaWithSrc
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

interface RaskraskaState : PageState {
    var raskraska: List<RaskraskaWithSrc>
    var isAdmin: Boolean
    var editId: Int?
}

class RaskraskaComponent(props: PageProps) : StandardPageComponent<RaskraskaState>(props) {
    init {
        state.raskraska = listOf()
        state.isAdmin = false
        Request.RaskraskaGetAll(400, 0).send(RaskraskaWithSrc.serializer().list) {
            setState {
                raskraska = it
            }
        }
        GlobalScope.launch {
            val r = Request.AdminCheck().send().parseAnswer().answerType == AnswerType.OK
            setState {
                isAdmin = r
            }
        }
    }

    private fun RBuilder.editButton(raskraska: RaskraskaWithSrc) {
        styledSpan {
            css {
                marginLeft = 10.px
                color = gray50Color
                textDecoration(TextDecorationLine.underline)
                fontStyle = FontStyle.italic
                cursor = Cursor.pointer
            }
            attrs.onClickFunction = {
                setState { editId = raskraska.raskraska.id }
            }
            +"редактировать"
        }
    }

    override fun StyledDOMBuilder<*>.page() {
        state.raskraska.forEach { raskraska ->
            styledDiv {
                css {
                    margin(50.px, 0.px)
                    overflow = Overflow.hidden
                    width = 90.pct
                }
                styledH3 {
                    css { color = redKraski }
                    +raskraska.raskraska.header
                    if (state.isAdmin) editButton(raskraska)
                }
                styledDiv {
                    css { margin(10.px, 0.px) }
                    if (raskraska.src != "") styledImg(src = raskraska.src) {
                        css {
                            width = 400.px
                        }
                    }
                    if (raskraska.src != "") styledImg(src = raskraska.src) {
                        css {
                            width = 400.px
                        }
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
                                formType = FormType.RaskraskaForm
                            }
                        }
                    }
                }
            }
        }
    }
}
