package kraski.client.elements.input

import kraski.client.gray50Color
import kotlinx.css.*
import kotlinx.css.properties.borderBottom
import kotlinx.html.DIV
import kotlinx.html.InputType
import kotlinx.html.LABEL
import kotlinx.html.id
import kotlinx.html.js.onBlurFunction
import kotlinx.html.js.onChangeFunction
import react.setState
import styled.StyledDOMBuilder
import styled.css
import styled.styledInput

class TextInputComponent : InputComponent<InputItemState>() {
    override fun StyledDOMBuilder<DIV>.containerBody() {
        css {
            flexDirection = FlexDirection.columnReverse
            marginTop = if (!state.isEmpty) 15.px else 0.px
        }
    }

    override fun StyledDOMBuilder<DIV>.inputBody() {
        styledInput(type = InputType.text, name = props.name) {
            attrs {
                value = props.value
                onChangeFunction = {
                    commonOnChange(it)
                    setState {
                        isEmpty = value.isEmpty()
                        isCorrect = props.validation(value)
                        isIncorrect = !isCorrect && wasCorrect
                    }
                }
                onBlurFunction = {
                    setState {
                        isCorrect = props.validation(value)
                        isIncorrect = !isCorrect
                    }
                }
                autoComplete = false
                id = "input-" + props.name
            }
            css {
                outline = Outline.none
                position = Position.relative
                border = "none"
                backgroundColor = Color.transparent
                borderBottom(1.px, BorderStyle.solid, gray50Color)
                height = 20.px
                zIndex = 2
                if (!state.isEmpty && state.isCorrect) {
                    backgroundColor = rgb(212, 235, 193)
                    borderBottomColor = Color.limeGreen
                }
                if (state.isIncorrect || (props.forceChecked && !state.isCorrect)) {
                    if (!state.isEmpty)
                    backgroundColor = Color.pink
                    borderBottomColor = Color.darkRed
                }
            }
        }
    }

    override fun StyledDOMBuilder<LABEL>.labelBody() {
        css {
            if (state.isEmpty) {
                top = 20.px
                fontSize = 16.px
            } else {
                top = 2.px
                fontSize = 12.px
            }
            if (!state.isEmpty && state.isCorrect) {
                backgroundColor = rgb(212, 235, 193)
                color = Color.darkSeaGreen
            }
            if (state.isIncorrect || (props.forceChecked && !state.isCorrect)) {
                backgroundColor = Color.pink
                color = Color.darkRed
            }
        }
        +props.title
    }
}