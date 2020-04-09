package kraski.client.pages

import kraski.client.*
import kraski.client.pages.join.Contest
import kraski.client.stucture.PageProps
import kraski.client.stucture.PageState
import kraski.client.stucture.StandardPageComponent
import kraski.common.AnswerType
import kraski.common.Request
import kraski.common.models.RaskraskaWithSrcs
import kraski.common.models.participants.FormType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.css.properties.TextDecorationLine
import kotlinx.css.properties.border
import kotlinx.css.properties.borderLeft
import kotlinx.css.properties.textDecoration
import kotlinx.html.js.onClickFunction
import kotlinx.serialization.list
import kraski.client.elements.image.imageInDiv
import react.RBuilder
import react.setState
import styled.*
import kotlin.math.abs

interface RaskraskaState : PageState {
    var raskraska: List<RaskraskaWithSrcs>
    var isAdmin: Boolean
    var editId: Int?
}

class RaskraskaComponent(props: PageProps) : StandardPageComponent<RaskraskaState>(props) {
    init {
        state.raskraska = listOf()
        state.isAdmin = false
        Request.RaskraskaGetAll(600, 400).send(RaskraskaWithSrcs.serializer().list) {
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

    private fun RBuilder.editButton(raskraska: RaskraskaWithSrcs) {
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
                    css {
                        borderLeft(2.px, BorderStyle.solid, redKraski)
                        margin(10.px, 0.px)
                        display = Display.flex
                    }
                    styledDiv {
                        css {
                            margin(0.px, 50.px, 20.px, 20.px)
                            width = 600.px
                            height = 400.px
                            position = Position.relative
                        }
                        if (raskraska.raskraskaSrc.first != "")
                            imageInDiv(raskraska.raskraskaSrc.first, "contain") {
                                css {
                                    +MainStyles.full
                                    backgroundColor = Color.white
                                }
                            }
                        if (raskraska.originalSrc.first != "")
                            imageInDiv(raskraska.originalSrc.second, "contain") {
                                css {
                                    +MainStyles.full
                                    backgroundColor = Color.white
                                    opacity = 0
                                    hover { opacity = 1 }
                                }
                            }
                    }
                    styledDiv {
                        styledH3 {
                            styledA(href = raskraska.raskraskaSrc.second) {
                                +"Скачать раскраску"
                            }
                        }
                        styledH3 {
                            styledA(href = raskraska.originalSrc.second) {
                                +"Скачать оригинал"
                            }
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
