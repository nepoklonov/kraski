package kraski.client.elements.input

import kotlinx.css.*
import kotlinx.html.DIV
import kotlinx.html.LABEL
import kotlinx.html.id
import kotlinx.html.js.onBlurFunction
import kotlinx.html.js.onChangeFunction
import react.setState
import styled.StyledDOMBuilder
import styled.css
import styled.styledOption
import styled.styledSelect

class SelectInputComponent : InputComponent<InputItemState>() {
    override fun StyledDOMBuilder<DIV>.containerBody() {
        css {
            flexDirection = FlexDirection.columnReverse
        }
    }

    override fun StyledDOMBuilder<DIV>.inputBody() {
        styledSelect {
            attrs {
                id = "input-" + props.name
                onChangeFunction = {
                    commonOnChange(it)
                    setState {
                        isEmpty = value == "-1"
                        isCorrect = props.validation(value)
                        isIncorrect = !isCorrect && wasCorrect
                    }
//                    (it.target as HTMLSelectElement).selectedIndex = state.value.toIntOrNull() ?: -1 + 1
                }
                onBlurFunction = {
                    setState {
                        isIncorrect = !isCorrect
                    }
                }
                this["value"] = if (state.value == "") if (props.value == "") "-1" else props.value else state.value
            }
            styledOption {
                attrs.value = "-1"
            }
            props.options.forEachIndexed { index, it ->
                styledOption {
                    attrs.label = it
                    attrs.value = index.toString()
                }
            }
            css {
                if (!state.isEmpty && state.isCorrect) {
                    backgroundColor = rgb(212, 235, 193)
                    borderColor = Color.limeGreen
                }
                if (state.isIncorrect || (props.forceChecked && !state.isCorrect)) {
                    backgroundColor = Color.pink
                    borderColor = Color.darkRed
                }
            }
        }
    }

    override fun StyledDOMBuilder<LABEL>.labelBody() {
        +props.title
        css {
            if (!state.isEmpty && state.isCorrect) {
                color = Color.darkSeaGreen
            }
            if (state.isIncorrect || (props.forceChecked && !state.isCorrect)) {
                color = Color.darkRed
            }
        }
    }

}