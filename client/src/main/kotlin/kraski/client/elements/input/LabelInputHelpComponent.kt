package kraski.client.elements.input

import kotlinx.html.DIV
import kotlinx.html.LABEL
import styled.StyledDOMBuilder

class LabelInputHelpComponent : InputComponent<InputItemState>() {
    override fun StyledDOMBuilder<DIV>.inputBody() {
    }

    override fun StyledDOMBuilder<LABEL>.labelBody() {
        +props.title
    }

    override fun StyledDOMBuilder<DIV>.containerBody() {
    }

}