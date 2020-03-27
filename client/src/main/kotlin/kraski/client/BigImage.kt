package kraski.client

import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import kotlinx.serialization.list
import kotlinx.serialization.serializer
import kraski.client.stucture.YamlListState
import kraski.client.stucture.initYamlListState
import kraski.client.stucture.updateYamlListState
import kraski.common.Request
import kraski.common.getPluralForm
import kraski.common.interpretation.ScaleType
import kraski.common.interpretation.put
import kraski.common.interpretation.x
import kraski.common.models.participants.FormType
import react.RBuilder
import react.RComponent
import react.RProps
import react.dom.a
import react.dom.br
import react.setState
import styled.css
import styled.styledDiv
import styled.styledH2

interface BigImageProps : RProps {
    var fileId: Int
    var formType: FormType?
    var close: () -> Unit
}

interface BigImageState : YamlListState<String> {
    var src: String
    var origSrc: String
}

class BigImageComponent : RComponent<BigImageProps, BigImageState>() {
    init {
        initYamlListState()
    }

    override fun RBuilder.render() {
        if (state.yaml.size == 0) {
            val formType = props.formType
            if (formType != null) {
                Request.ParticipantsImagesGetInfo(formType, props.fileId)
                    .send(String.serializer().list, ::updateYamlListState)
                Request.ParticipantsImagesGetVersion(props.fileId, 500 x 500 put ScaleType.OUTSIDE).send(String.serializer()) {
                    setState { src = it }
                }
                Request.ParticipantsImagesGetOriginal(props.fileId).send(String.serializer()) {
                    setState { origSrc = it }
                }
            }
        }
        styledDiv {
            css {
                display = Display.flex
                justifyContent = JustifyContent.center
                alignItems = Align.center
                position = Position.fixed
                top = 0.px
                left = 0.px
                width = 100.pct
                height = 100.pct
                zIndex = 10
            }
            styledDiv {
                attrs.onClickFunction = {
                    props.close()
                }
                css {
                    position = Position.absolute
                    width = 100.pct
                    height = 100.pct
                    backgroundColor = Color.black
                    opacity = 0.7
                }
            }
            if (state.yaml.size != 0) {
                styledDiv {
                    css {
                        width = 800.px
                        height = 500.px
                        backgroundColor = Color.white
                        zIndex = 3
                        display = Display.flex
                    }
                    styledDiv {
                        css {
                            backgroundImage = Image("url('${state.src}')")
                            backgroundRepeat = BackgroundRepeat.noRepeat
                            backgroundSize = "cover"
                            backgroundPosition = "center center"
                            width = 500.px
                            height = 500.px
                        }
                    }
                    styledDiv {
                        css {
                            padding(20.px)
                            fontSize = 16.pt
                        }
                        styledH2 {
                            css.margin(10.px)
                            +state.yaml[0]
                        }
                        br { }
                        +state.yaml.let { "${it[1]}," }
                        br {}
                        if (state.yaml[2] != "") {
                            +state.yaml[2].let { "$it " + it.toInt().getPluralForm("год", "года", "лет") + "," }
                            br { }
                        }
                        +state.yaml[3]
                        br {}
                        a(target = "blank", href = state.origSrc) {
                            +"Открыть полную версию"
                        }
                    }
                }
            }
        }
    }
}
