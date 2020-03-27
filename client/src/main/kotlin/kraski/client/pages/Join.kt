package kraski.client.pages

import kraski.client.pages.join.Contest
import kraski.client.stucture.PageProps
import kraski.client.stucture.PageState
import kraski.client.stucture.StandardPageComponent
import kraski.common.interpretation.Documents
import kraski.common.interpretation.Pages
import kraski.common.models.participants.FormType
import kotlinx.css.*
import kotlinx.css.properties.borderRight
import kotlinx.html.ATarget
import kotlinx.html.js.onClickFunction
import kotlinx.serialization.list
import kraski.client.*
import kraski.client.stucture.header.serverState
import kraski.common.Bio
import kraski.common.JoinJSON
import kraski.common.Request
import kraski.common.interpretation.ImageDirs
import kraski.common.interpretation.JsonRef
import react.RBuilder
import react.dom.a
import react.dom.br
import react.router.dom.navLink
import react.setState
import styled.*

fun getFormType(current: String): FormType? {
    return when (current) {
        Pages.Join.art.path -> FormType.Art
        Pages.Join.photos.path -> FormType.Photos
        Pages.Join.music.path -> FormType.Music
        Pages.Join.dance.path -> FormType.Dance
        Pages.Join.scientific.path -> FormType.Scientific
        Pages.Join.literature.path -> FormType.Literature
        Pages.Join.professional.path -> FormType.Professional
        Pages.join.path -> null
        else -> console.log(current).run { error("what and why??") }
    }
}

interface JoinState : PageState {
    var adding: Boolean
    var addForm: FormType?
    var joinJSON: List<JoinJSON>
    var bio: Bio?
}

class JoinComponent(props: PageProps) : StandardPageComponent<JoinState>(props) {
    init {
        state.adding = false
        state.addForm = null
        state.bio = null
        serverState(JoinState::joinJSON, listOf(), Request.GetJson(JsonRef.JoinJson), JoinJSON.serializer().list)
    }

    private fun RBuilder.joinElement(formType: FormType) {

        if (state.joinJSON.isNotEmpty()) styledDiv {
            val joinJsonElement = state.joinJSON.first { it.name == formType.folder.name }
            css {
                display = Display.flex
                justifyContent = JustifyContent.spaceBetween
                alignItems = Align.center
                width = 700.px
                height = 150.px
                padding(10.px, 0.px)
            }
            styledImg(src = (ImageDirs.people file joinJsonElement.portrait).path) {
                css {
                    height = 100.pct
                }
            }
            styledDiv {
                css {
                    display = Display.flex
                    flexWrap = FlexWrap.wrap
                    justifyContent = JustifyContent.center
                    height = 90.pct
                    width = 120.px
                    marginBottom = 10.px
                }
                styledImg(src = (ImageDirs.svg file joinJsonElement.icon).path) {
                    css {
                        width = 90.pct
                    }
                }
                styledButton {
                    css {
                        display = Display.block
                        width = 100.pct
                        cursor = Cursor.pointer
                        fontWeight = FontWeight.bold
                        backgroundColor = redKraski
                        color = Color.white
                        outline = Outline.none
                        fontSize = 16.px
                        borderColor = Color.red
                        borderRadius = 5.px
                    }
                    attrs.onClickFunction = {
                        it.preventDefault()
                        setState {
                            adding = true
                            addForm = formType
                        }
                    }
                    +"Участвовать"
                }
            }
            styledDiv {
                css {
                        width = 350.px
                }
                styledP {
                    css {
                        fontSize = 18.px
                        color = redKraski
                        paddingBottom = 10.px
                    }
                    +formType.title
                }

                styledA(href = Documents.getOfficialPDF(formType), target = ATarget.blank) {
                    +"Скачать положение"
                }
                br {}
                styledA(href = "#") {
                    +joinJsonElement.bio.name
                    attrs.onClickFunction = {
                        it.preventDefault()
                        setState {
                            bio = joinJsonElement.bio
                        }
                    }
                }
                br {}
                navLink((Pages.gallery file joinJsonElement.name).path) {
                    +"Работы участников"
                }
                br {}
            }
        }
    }

    override fun StyledDOMBuilder<*>.page() {
        if (!state.adding) {
            Pages.Join.run { listOf(art, photos, music, dance, professional, scientific, literature) }
                    .map { getFormType(it.path) }.forEach { form ->
                        yellowLine(700.px)
                        joinElement(form!!)
                    }
            yellowLine(700.px)
            state.bio?.let {
                child(FrameComponent::class) {
                    attrs {
                        close = {
                            setState {
                                bio = null
                            }
                        }
                        width = 60.pct
                        height = 80.pct
                        content = {
                            styledDiv {
                                styledH3 {
                                    css {
                                        textAlign = TextAlign.center
                                        fontSize = 18.px
                                    }
                                    +it.name
                                }
                                styledH4 {
                                    css {
                                        textAlign = TextAlign.center
                                        color = gray50Color
                                    }
                                    +it.date
                                }
                                styledP {
                                    css {
                                        padding(20.px)
                                    }
                                    +it.text
                                }
                            }
                        }
                    }
                }
            }
        } else {
            val addForm = state.addForm
            if (addForm != null) {
                child(FrameComponent::class) {
                    attrs {
                        width = 60.pct
                        height = 80.pct
                        close = {
                            setState {
                                adding = false
                            }
                        }
                        content = {
                            child(Contest::class) {
                                attrs {
                                    this.formType = addForm
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}