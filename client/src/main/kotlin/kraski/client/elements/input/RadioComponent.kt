package kraski.client.elements.input

import kraski.client.gray50Color
import kotlinx.css.*
import kotlinx.html.DIV
import kotlinx.html.InputType
import kotlinx.html.LABEL
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import react.setState
import styled.*

class RadioInputComponent : InputComponent<InputItemState>() {
    override fun StyledDOMBuilder<DIV>.containerBody() {
        css {
            flexDirection = FlexDirection.column
        }
    }

    override fun StyledDOMBuilder<DIV>.inputBody() {
        props.options.forEachIndexed { index, s ->
            styledDiv {
                css {
                    display = Display.inlineFlex
                    width = 100.pct
                    flexDirection = FlexDirection.row
                    if (index != 0) marginTop = 10.px
                }
                styledInput(type = InputType.radio, name = props.name) {
                    attrs {
                        onChangeFunction = {
                            commonOnChange(it)
                            setState {
                                isEmpty = value == "-1"
                                isCorrect = props.validation(value)
                            }
                        }
                        value = index.toString()
                        checked = props.value == value
                        autoComplete = false
                        id = "input-" + props.name + "-$index"
                    }
                    css {
                        outline = Outline.none
                        marginRight = 5.px
                    }
                }

                styledLabel {
                    if (state.isEmpty) attrs["htmlFor"] = "input-" + props.name + "-$index"
                    css {
                        position = Position.relative
                        color = gray50Color
                    }
                    +s
                }
            }
        }
    }

    override fun StyledDOMBuilder<DIV>.label() {

    }

    override fun StyledDOMBuilder<LABEL>.labelBody() {

    }
}
