package kraski.client.elements.input

import kraski.client.gray50Color
import kotlinx.css.*
import kotlinx.css.properties.borderBottom
import kotlinx.html.DIV
import kotlinx.html.LABEL
import kotlinx.html.id
import react.RClass
import react.RProps
import react.dom.div
import react.setState
import styled.StyledDOMBuilder
import styled.css
import styled.styledDiv
import kotlin.browser.document

external interface CKEditorProps : RProps {
    var editor: dynamic
    var data: String
    var config: dynamic
    var onInit: (dynamic) -> Unit
    var onChange: (dynamic, dynamic) -> Unit
}

@JsModule("@ckeditor/ckeditor5-react")
external val CKEditor: RClass<CKEditorProps>

@JsModule("@ckeditor/ckeditor5-build-decoupled-document")
external val DecoupledEditor: RClass<CKEditorProps>


class HTMLInputComponent : InputComponent<InputItemState>() {
    override fun StyledDOMBuilder<DIV>.containerBody() {
        css {
            flexDirection = FlexDirection.columnReverse
            marginTop = if (!state.isEmpty) 15.px else 0.px
        }
    }

    override fun StyledDOMBuilder<DIV>.inputBody() {
        styledDiv {
            css {
                outline = Outline.none
                position = Position.relative
                border = "none"
                backgroundColor = Color.transparent
                borderBottom(1.px, BorderStyle.solid, gray50Color)
                zIndex = 2
                if (!state.isEmpty && state.isCorrect) {
                    backgroundColor = rgb(212, 235, 193)
                    borderBottomColor = Color.limeGreen
                }
                if (state.isIncorrect || (props.forceChecked && !state.isCorrect)) {
                    backgroundColor = Color.pink
                    borderBottomColor = Color.darkRed
                }
                fontWeight = FontWeight.initial
            }
            attrs.id = "input-" + props.name
            CKEditor {
                attrs.editor = DecoupledEditor
                attrs.data = props.value
                attrs.config = js("""{
                        placeholder: '',
                        toolbar: [ 'bold', 'italic', 'link' ]
                    }""")
                attrs.onInit = {
                    val toolbar = it.ui.view.toolbar.element
                    document.getElementById("editor-toolbar")!!.appendChild(toolbar)
                }
                attrs.onChange = fun(_, editor) {
                    props.valueUpdate(props.name, editor.getData() as String)
                    setState {
                        value = editor.getData() as String
                        isEmpty = value.isEmpty()
                        isCorrect = props.validation(value)
                        isIncorrect = !isCorrect && wasCorrect
                    }
                }
            }
        }
        div {
            attrs.id = "editor-toolbar"
        }
    }

    override fun StyledDOMBuilder<LABEL>.labelBody() {
        css {
            if (state.isEmpty) {
                top = 76.px
                paddingLeft = 20.px
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