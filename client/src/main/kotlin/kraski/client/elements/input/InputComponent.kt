package kraski.client.elements.input

import kraski.client.gray50Color
import kraski.common.DisplayType
import kraski.common.models.participants.FormType
import kotlinx.css.*
import kotlinx.html.DIV
import kotlinx.html.LABEL
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.events.Event
import react.*
import styled.StyledDOMBuilder
import styled.css
import styled.styledDiv
import styled.styledLabel

interface InputItemProps : RProps {
    var formType: FormType
    var type: DisplayType
    var name: String
    var title: String
    var width: LinearDimension
    var isExpected: Boolean
    var time: String
    var validation: (String) -> Boolean
    var valueUpdate: (String, String) -> Unit
    var enable: Boolean
    var options: List<String>
    var forceChecked: Boolean
    var value: String
}

interface InputItemState : RState {
    var value: String
    var isEmpty: Boolean
    var isLoading: Boolean
    var isCorrect: Boolean
    var isIncorrect: Boolean
}


abstract class InputComponent<T : InputItemState> : RComponent<InputItemProps, T>() {
    val wasCorrect: Boolean
        get() = state.isCorrect || state.isIncorrect
    val commonOnChange = { it: Event ->
        val newValue = when (props.type) {
            DisplayType.CheckBox -> (it.target as HTMLInputElement).checked.toString()
            DisplayType.Text -> (it.target as HTMLInputElement).value
            DisplayType.HTML -> (it.target as HTMLInputElement).value
            DisplayType.File -> ""
            DisplayType.Select -> ((it.target as HTMLSelectElement).selectedIndex - 1).toString()
            DisplayType.Radio -> (it.target as HTMLInputElement).value
            DisplayType.Submit -> error("Submit input updating is impossible")
            DisplayType.Hidden -> error("Hidden input updating is impossible")
            DisplayType.Label -> error("Label updating is impossible")
        }
        props.valueUpdate(props.name, newValue)
        setState {
            value = newValue
        }
    }

    abstract fun StyledDOMBuilder<DIV>.containerBody()
    abstract fun StyledDOMBuilder<DIV>.inputBody()
    abstract fun StyledDOMBuilder<LABEL>.labelBody()


    open fun StyledDOMBuilder<DIV>.label() {
        styledLabel {
            attrs["htmlFor"] = "input-" + props.name
            css {
                position = Position.relative
                color = gray50Color
            }
            labelBody()
        }
    }

    init {
        state.isEmpty = true
        state.isLoading = false
        state.isCorrect = false
        state.isIncorrect = false
        state.value = ""
    }

    override fun componentDidMount() {
        if (props.value != "" && props.type != DisplayType.File) setState {
            value = props.value
            isEmpty = value.isEmpty()
            isCorrect = props.validation(value)
            isIncorrect = !isCorrect
        }
    }

    override fun componentDidUpdate(prevProps: InputItemProps, prevState: T, snapshot: Any) {
        //TODO(костыль убрать)
        if (props.type == DisplayType.CheckBox && state.value == "") {
            setState { value = "false" }
        }
        if (props.type == DisplayType.Radio && state.value == "") {
            setState { value = "-1" }
        }
    }

    override fun RBuilder.render() {

        styledDiv {
            css {
                display = if (props.isExpected) Display.inlineFlex else Display.none
                width = props.width
                flexDirection = FlexDirection.column
                marginTop = 30.px
            }

            containerBody()
            inputBody()
            label()
        }
    }
}