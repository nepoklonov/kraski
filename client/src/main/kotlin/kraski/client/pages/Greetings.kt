package kraski.client.pages

import kraski.client.FrameComponent
import kraski.client.GalleryBox
import kraski.client.gray20Color
import kraski.client.pages.join.Contest
import kraski.client.send
import kraski.client.stucture.PageProps
import kraski.client.stucture.StandardPageComponent
import kraski.client.stucture.YamlListState
import kraski.client.stucture.updateYamlListState
import kraski.common.FileAnswer
import kraski.common.Request
import kraski.common.interpretation.ImageDirs
import kraski.common.interpretation.JsonRef
import kraski.common.models.Review
import kraski.common.models.participants.FormType
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import kotlinx.serialization.list
import kotlinx.serialization.serializer
import react.dom.p
import react.setState
import styled.*

interface GreetingsState : YamlListState<String> {
    var contest: Boolean //TODO: rename Contest class to "Form"
    var reviews: List<Review>
}

class Greetings(pageProps: PageProps) : StandardPageComponent<GreetingsState>(pageProps) {
    init {
        state.contest = false
        Request.GetJson(JsonRef.ChuvashiaArchiveJson).send(String.serializer().list, ::updateYamlListState)
        Request.ReviewsGetAll().send(Review.serializer().list) {
            setState {
                reviews = it
            }
        }
    }

    override fun StyledDOMBuilder<*>.page() {
        if (state.yaml != undefined) {
            styledDiv {
                css {
                    display = Display.flex
                    flexWrap = FlexWrap.wrap
                    justifyContent = JustifyContent.spaceBetween
                }
                child(GalleryBox::class) {
                    attrs {
                        content = state.yaml.map {
                            (ImageDirs.chuvashia file it).path
                        }.mapIndexed { index, src ->
                            FileAnswer(index, src, src)
                        }
                        horizontalAmount = 5
                        proportion = 1.424
                        zoom = 0.8
                        getImages = { _, _ -> }
                        current = props.current
                        infoBlock = { }
                    }
                }

                styledSpan {
                    css {
                        width = 100.pct
                        fontSize = 18.px
                    }
                    styledA(href = "#") {
                        attrs.onClickFunction = {
                            it.preventDefault()
                            setState {
                                contest = true
                            }
                        }
                        +"Оставить отзыв"
                    }
                }

                if (state.contest) {
                    child(FrameComponent::class) {
                        attrs {
                            width = 60.pct
                            height = 80.pct
                            close = {
                                setState {
                                    contest = false
                                }
                            }
                            content = {
                                css {
                                    overflow = Overflow.auto
                                }
                                child(Contest::class) {
                                    attrs {
                                        formType = FormType.ReviewForm
                                    }
                                }
                            }
                        }
                    }
                }

                if (state.reviews != undefined) {
                    state.reviews.forEach {
                        styledDiv {
                            css {
                                width = 400.px
                                margin(20.px)
                                padding(20.px, 40.px, 20.px, 15.px)
                                backgroundColor = gray20Color
                            }
                            styledP {
                                css {
                                    fontWeight = FontWeight.bold
                                    fontSize = 14.pt
                                }
                                +it.fio
                            }
                            p {
                                +it.review
                            }
                        }
                    }
                }
            }
        }
    }
}