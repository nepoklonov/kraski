package kraski.client.elements.input

import kraski.common.interpretation.Documents
import kraski.common.models.participants.FormType
import kotlinx.css.*
import kotlinx.html.DIV
import kotlinx.html.InputType
import kotlinx.html.LABEL
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import react.dom.a
import react.dom.br
import react.setState
import styled.StyledDOMBuilder
import styled.css
import styled.styledInput

class CheckBoxInputComponent : InputComponent<InputItemState>() {
    override fun StyledDOMBuilder<DIV>.containerBody() {
        css {
            flexDirection = FlexDirection.row
        }
    }

    override fun StyledDOMBuilder<DIV>.inputBody() {
        styledInput(type = InputType.checkBox, name = props.name) {
            attrs {
                checked = props.value.toBoolean()
                onChangeFunction = {
                    commonOnChange(it)
                    setState {
                        isEmpty = !value.toBoolean()
                        isCorrect = props.validation(value)
                    }
                }
                autoComplete = false
                id = "input-" + props.name
            }
            css {
                outline = Outline.none
                marginRight = 5.px
                if (state.isIncorrect || (props.forceChecked && !state.isCorrect)) {
                    before {
                        color = Color.red
                        position = Position.relative
                        top = (-1).px
                        left = (-7).px
                        content = QuotedString("!")
                    }
                }
            }
        }
    }

    override fun StyledDOMBuilder<LABEL>.labelBody() {
        css {
            top = (-3).px
        }
        when (props.title) {
            "$1" -> {
                +"Я даю согласие на обработку моих персональных данных."
                br {}
                a(target = "_blank", href = "/documents/fz.pdf") {
                    +"Ознакомиться с требованиями федерального закона о персональных данных"
                }
            }
            "$2" -> {
                +"Я ознакомлен с "
                a(target = "_blank", href = Documents.getOfficialPDF(props.formType)) {
                    +"положением о "
                    if (props.formType in arrayOf(FormType.Scientific, FormType.Literature)) +"Конференции" else +"Конкурсе"
                }
            }
            else -> {
                +props.title
            }
        }
    }
}
