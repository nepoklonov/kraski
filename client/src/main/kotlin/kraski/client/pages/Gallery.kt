package kraski.client.pages

import kraski.client.stucture.*
import kraski.common.FileAnswer
import kraski.common.Request
import kraski.common.getPluralForm
import kraski.common.interpretation.ScaleType
import kraski.common.interpretation.put
import kraski.common.interpretation.x
import kraski.common.models.participants.FormType
import kotlinx.css.*
import kotlinx.serialization.list
import kraski.client.*
import kraski.common.interpretation.Pages
import react.RComponent
import react.RProps
import react.dom.a
import react.dom.br
import react.router.dom.navLink
import styled.*

private val updateYaml: RComponent<out RProps, out YamlListState<FileAnswer>>.(FormType, Int, Int) -> Unit
    get() = { type, width, height ->
        Request.ParticipantsImagesGetAll(type, width x height put ScaleType.OUTSIDE).send(FileAnswer.serializer().list, ::updateYamlListState)
    }

fun getGalleryType(current: String): FormType? {
    return when (current) {
        Pages.Gallery.art.path -> FormType.Art
        Pages.Gallery.photos.path -> FormType.Photos
        Pages.Gallery.music.path -> FormType.Music
        Pages.Gallery.dance.path -> FormType.Dance
        Pages.Gallery.scientific.path -> FormType.Scientific
        Pages.Gallery.literature.path -> FormType.Literature
        Pages.Gallery.professional.path -> FormType.Professional
        Pages.gallery.path -> null
        else -> console.log(current).run { error("what and why??") }
    }
}

class GalleryComponent(props: PageProps) : StandardPageComponent<YamlListState<FileAnswer>>(props) {

    object BoxProps {
        const val horizontalAmount = 4
        const val proportion = 0.75
        const val zoom = 0.8
    }

    init {
        initYamlListState()
    }

    override fun StyledDOMBuilder<*>.page() {
        val current = getGalleryType(props.current)
        if (current == null) {
            styledDiv {
                css {
                    child("a") {
                        width = 450.px
                        display = Display.block
                        margin(10.px)
                    }
                }
                Section.Gallery.pages.forEach {
                    navLink(it.url) {
                        styledButton {
                            css {
                                display = Display.block
                                width = 100.pct
                                height = 100.pct
                                cursor = Cursor.pointer
                                fontWeight = FontWeight.bold
                                if (it.url == props.current) {
                                    backgroundColor = redKraski
                                    color = Color.white
                                } else {
                                    backgroundColor = lightYellow
                                    hover {backgroundColor = mediumYellow}
                                    active {backgroundColor = yellowKraski}
                                    color = redKraski
                                }
                                padding(8.px)
                                outline = Outline.none
                                borderRadius = 3.px
                            }
                            +it.title
                        }
                    }
                }
            }
        } else if (current == FormType.Art || current == FormType.Photos) {
            navLink(Pages.gallery.path) {
                styledSpan {
                    css {
                        color = redKraski
                        marginBottom = 15.px
                        display = Display.block
                    }
                    +"<- Назад к галереям и спискам участников"
                }
            }
            child(GalleryBox::class) {
                attrs {
                    content = state.yaml
                    horizontalAmount = BoxProps.horizontalAmount
                    proportion = BoxProps.proportion
                    zoom = BoxProps.zoom
                    getImages = { w, h -> updateYaml(getGalleryType(props.current)!!, w, h) }
                    this.current = props.current
                    infoBlock = { it ->
                        styledDiv {
                            css {
                                width = 300.px
                                padding(20.px)
                                fontSize = 16.pt
                            }
                            styledH2 {
                                css.margin(10.px)
                                +it.info!![0]
                            }
                            br { }
                            +"${it.info!![1]},"
                            br {}
                            if (it.info!![2] != "") {
                                +it.info!![2].let { "$it " + it.toInt().getPluralForm("год", "года", "лет") + "," }
                                br { }
                            }
                            +it.info!![3]
                            br {}
                            a(target = "blank", href = it.bigSrc) {
                                +"Открыть полную версию"
                            }
                        }
                    }
                }
            }
        } else {
            navLink(Pages.gallery.path) {
                styledSpan {
                    css {
                        color = redKraski
                    }
                    +"<- Назад к галереям и спискам участников"
                }
            }
            child(AntiGallery::class) {
                attrs {
                    this.formType = current
                }
            }
        }
    }
}