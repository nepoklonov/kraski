package kraski.client.pages.about

import kraski.client.GalleryBox
import kraski.client.send
import kraski.client.stucture.*
import kraski.common.Request
import kraski.common.SubGallery
import kotlinx.css.*
import kotlinx.serialization.list
import react.RComponent
import react.RProps
import styled.StyledDOMBuilder
import styled.css
import styled.styledH2

private val updateYaml: RComponent<out RProps, out YamlListState<SubGallery>>.(Int, Int) -> Unit
    get() = { width, height ->
        Request.AboutGetPhotos(width, height).send(SubGallery.serializer().list, ::updateYamlListState)
    }

class PhotosComponent(props: PageProps) : StandardPageComponent<YamlListState<SubGallery>>(props) {

    object BoxProps {
        const val horizontalAmount = 7
        const val proportion = 0.75
        const val zoom = 0.8
    }

    init {
        initYamlListState()
        updateYaml(0, 0)
    }

    override fun StyledDOMBuilder<*>.page() {
        state.yaml.forEach {
            styledH2 {
                css {
                    margin(10.px, 0.px, 5.px, 0.px)
                }
                +"Земля Калевалы — ${it.title}"
            }
            child(GalleryBox::class) {
                attrs {
                    content = it.list
                    horizontalAmount = BoxProps.horizontalAmount
                    proportion = BoxProps.proportion
                    zoom = BoxProps.zoom
                    getImages = { _, _ -> }
                    current = props.current
                    infoBlock = { }
                }
            }
        }
    }
}