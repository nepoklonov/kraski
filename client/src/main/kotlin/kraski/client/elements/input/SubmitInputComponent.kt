package kraski.client.elements.input

import kotlinx.css.*
import kotlinx.css.properties.border
import kraski.client.gray50Color
import kotlinx.html.*
import styled.StyledDOMBuilder
import styled.css
import styled.styledInput

class SubmitInputComponent : InputComponent<InputItemState>() {
    override fun StyledDOMBuilder<DIV>.inputBody() {
        styledInput(type = InputType.submit, name = props.name) {
            attrs {
                autoComplete = false
                id = "input-" + props.name
            }
            css {
                outline = Outline.none
                height = 30.px
                marginBottom = 100.px //TODO marginBottom in all pages
                if (props.enable) backgroundColor = Color("#ddd")
                if (!props.enable) border(1.px, BorderStyle.solid, gray50Color)
                color = if (props.enable) Color.black else Color("#888")
                if (props.enable) cursor = Cursor.pointer
            }
        }
    }

    override fun StyledDOMBuilder<LABEL>.labelBody() {

    }

    override fun StyledDOMBuilder<DIV>.containerBody() {
    }

}