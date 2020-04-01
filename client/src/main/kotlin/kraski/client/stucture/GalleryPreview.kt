package kraski.client.stucture

import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import kotlinx.serialization.list
import kraski.client.MainStyles
import kraski.client.elements.frames.BigImage
import kraski.client.elements.frames.BigImageProps
import kraski.client.elements.image.imageInDiv
import kraski.client.send
import kraski.common.FileAnswer
import kraski.common.Request
import kraski.common.interpretation.ImageDirs
import kraski.common.interpretation.ScaleType
import kraski.common.interpretation.put
import kraski.common.interpretation.x
import kraski.common.models.participants.FormType
import react.*
import react.dom.a
import styled.StyledComponents.css
import styled.css
import styled.styledDiv

interface GalleryPreviewState : YamlListState<FileAnswer> {
    var index: Int
    var bigFile: FileAnswer?
}

class GalleryPreview : RComponent<RProps, GalleryPreviewState>() {
    init {
        state.index = 0
        state.bigFile = null
        initYamlListState()
        Request.ParticipantsImagesGetAll(FormType.Art, 200 x 140 put ScaleType.OUTSIDE).send(FileAnswer.serializer().list) {
            setState { yaml = (yaml + it).toMutableList() }
        }
        Request.ParticipantsImagesGetAll(FormType.Photos, 200 x 140 put ScaleType.OUTSIDE).send(FileAnswer.serializer().list) {
            setState { yaml = (yaml + it).toMutableList() }
        }
    }

    override fun RBuilder.render() {
        if (state.yaml.isNotEmpty()) {
            styledDiv {
                css {
                    display = Display.flex
                    alignItems = Align.center
                }
                imageInDiv((ImageDirs.design file "three-left.png").path, "contain", 20.px, 40.px) {
                    css {
                        +MainStyles.tapable
                    }
                    attrs.onClickFunction = { _ ->
                        setState { index = (index - 1) % yaml.size }
                    }
                }
                imageInDiv(state.yaml[state.index].src, "cover", 200.px, 120.px) {
                    attrs.onClickFunction = {
                        setState {
                            bigFile = yaml[index]
                        }
                    }
                    css {
                        cursor = Cursor.pointer
                        margin(0.px, 10.px)
                    }
                }
                imageInDiv((ImageDirs.design file "three.png").path, "contain", 20.px, 40.px) {
                    css {
                        +MainStyles.tapable
                    }
                    attrs.onClickFunction = { _ ->
                        setState { index = (index + 1) % yaml.size }
                    }
                }
            }
        }
        state.bigFile?.let {
            child<BigImageProps, BigImage> {
                attrs.src = it.bigSrc!!
                attrs.infoBlock = {
                    styledDiv {
                        css {
                            margin(0.px, 20.px)
                        }
                        a(target = "blank", href = it.bigSrc) {
                            +"Открыть полную версию"
                        }
                    }
                }
                attrs.close = {
                    setState {
                        bigFile = null
                    }
                }
            }
        }
    }
}