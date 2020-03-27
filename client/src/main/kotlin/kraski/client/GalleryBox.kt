package kraski.client

import kraski.client.elements.frames.BigImage
import kraski.client.elements.frames.BigImageProps
import kraski.common.FileAnswer
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import org.w3c.dom.Element
import react.*
import react.dom.tbody
import styled.*

interface GalleryBoxProps : RProps {
    var getImages: (Int, Int) -> Unit
    var content: List<FileAnswer>
    var horizontalAmount: Int
    var proportion: Double
    var zoom: Double
    var current: String
    var infoBlock: RBuilder.(FileAnswer) -> Unit
}

interface GalleryBoxState : RState {
    var imageHeight: Int
    var bigFile: FileAnswer?
}

class GalleryBox : RComponent<GalleryBoxProps, GalleryBoxState>() {

    init {
        state.imageHeight = 0
        state.bigFile = null
    }

    private val tableRef = createRef<Element>()

    override fun componentDidUpdate(prevProps: GalleryBoxProps, prevState: GalleryBoxState, snapshot: Any) {
        tableRef.current?.getBoundingClientRect()?.width?.let {
            val h = (it / props.horizontalAmount * props.proportion).toInt()
            val w = (it / props.horizontalAmount).toInt()
            if (state.imageHeight != h || props.current != prevProps.current) {
                setState {
                    imageHeight = h
                }
                props.getImages(w, h)
            }
        }
    }

    override fun componentDidMount() {
        tableRef.current?.getBoundingClientRect()?.width?.let {
            val h = (it / props.horizontalAmount * props.proportion).toInt()
            val w = (it / props.horizontalAmount).toInt()
            setState {
                imageHeight = h
            }
            props.getImages(w, h)
        }
    }

    override fun RBuilder.render() {
        state.bigFile?.let {
            child<BigImageProps, BigImage> {
                attrs.src = it.bigSrc!!
                attrs.infoBlock = fun RBuilder.() {
                    (props.infoBlock)(it)
                }
                attrs.close = {
                    setState {
                        bigFile = null
                    }
                }
            }
        }
        styledTable {
            ref = tableRef
            css {
                width = 100.pct
            }
            tbody {
                for (i in props.content.indices step props.horizontalAmount) {
                    styledTr {
                        css.height = state.imageHeight.px
                        for (j in i until i + props.horizontalAmount) {
                            styledTd {
                                css {
                                    width = (100 / props.horizontalAmount).pct
                                    height = 100.pct
                                    display = Display.inlineFlex
                                    justifyContent = JustifyContent.center
                                    alignContent = Align.center
                                }
                                if (props.content.size > j) {
                                    styledDiv {
                                        attrs.onClickFunction = {
                                            setState {
                                                bigFile = props.content[j]
                                            }
                                        }
                                        css {
                                            cursor = Cursor.pointer
                                            backgroundImage = Image("url('${props.content[j].src}')")
                                            backgroundRepeat = BackgroundRepeat.noRepeat
                                            backgroundSize = "cover"
                                            backgroundPosition = "center center"
                                            width = (100 * props.zoom).pct
                                            height = (state.imageHeight * props.zoom).px
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}