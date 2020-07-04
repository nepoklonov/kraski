package kraski.client.pages

import kraski.client.elements.image.imageInDiv
import kraski.client.gray70Color
import kraski.client.pages.join.Contest
import kraski.client.parseAnswer
import kraski.client.parseAnswerBody
import kraski.client.send
import kraski.client.stucture.PageProps
import kraski.client.stucture.PageState
import kraski.client.stucture.StandardPageComponent
import kraski.common.AnswerType
import kraski.common.FileAnswer
import kraski.common.Request
import kraski.common.interpretation.ScaleType.OUTSIDE
import kraski.common.interpretation.put
import kraski.common.interpretation.x
import kraski.common.models.InputField
import kraski.common.models.participants.FormType
import kraski.common.quote
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.css.properties.border
import kotlinx.html.ATarget
import kotlinx.html.AreaTarget
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onInputFunction
import kotlinx.serialization.list
import kotlinx.serialization.serializer
import org.w3c.dom.HTMLInputElement
import react.RBuilder
import react.dom.a
import react.dom.b
import react.dom.br
import react.dom.defaultValue
import react.setState
import styled.*

interface AdminState : PageState {
    var isAdmin: Boolean
    var section: Int
    var from: String
    var fromValue: String
    var participants: List<List<InputField>>?
    var files: MutableMap<Int, FileAnswer>
}

@Suppress("UNCHECKED_CAST")
class AdminComponent(pageProps: PageProps) : StandardPageComponent<AdminState>(pageProps) {
    init {
        state.section = 0
        state.files = mutableMapOf()
        state.from = "0"
        state.fromValue = "0"
    }

    var sectionsAmount = 0

    private inline fun RBuilder.section(title: String, isSelected: RBuilder.() -> Unit) {
        val id = sectionsAmount + 1
        styledDiv {
            styledSpan {
                css {
                    width = 100.pct
                    fontSize = 18.px
                }
                styledA(href = "#") {
                    attrs.onClickFunction = {
                        it.preventDefault()
                        setState {
                            participants = undefined
                            section = if (section == 0) id else 0
                        }
                    }
                    +title
                }
            }
            if (state.section == id) {
                isSelected()
            }
        }
        sectionsAmount++
    }


    fun RBuilder.goNext() = styledDiv {
        css {
            display = Display.flex
            justifyContent = JustifyContent.spaceBetween
        }
        fun RBuilder.leftRight(vararg numbers: Int) = numbers.forEach { number ->
            styledDiv {
                a(href = "#") {
                    +if (number < 0) "$number <-" else "-> +$number"
                    attrs.onClickFunction = {
                        it.preventDefault()
                        val newFrom = (state.from.toInt() + number)
                        setState { from = newFrom.toString() }
                    }
                }
            }
        }


        styledDiv {
            leftRight(-50, -200, -1000)
        }

        styledSpan {
            +"${state.from} -- ${state.from.toInt() + 49}"
        }

        styledDiv {
            leftRight(50, 200, 1000)
        }
    }

    private fun RBuilder.forms(title: String, formType: FormType) {
        section("Анкеты в разделе ${title.quote()}") {
            val participants = state.participants
            styledSpan {
                +" начиная с: "
            }
            styledInput {
                css {
                    width = 40.px
                    height = 20.px
                }
                attrs.defaultValue = state.fromValue
                attrs.onInputFunction = {
                    val v = (it.target as HTMLInputElement).value
                    setState {
                        fromValue = if (v == "") "0" else v
                    }
                }
            }
            styledButton {
                css {
                    width = 60.px
                    height = 23.px
                }
                +"Искать"
                attrs.onClickFunction = {
                    setState {
                        from = fromValue
                    }
                }
            }
            goNext()
            if (participants != undefined) {
                participants
                    .filterIndexed { index, _ -> index in state.from.toInt()..(state.from.toInt() + 50) }
                    .forEach { p ->
                        styledDiv {
                            css {
                                border(1.px, BorderStyle.solid, gray70Color)
                                padding(5.px)
                                display = Display.flex
                            }
                            if (state.files != undefined) {
                                p.find { it.name == "imageFileId" }?.let {
                                    styledA(href = state.files[it.value.toInt()]?.bigSrc ?: "", target = ATarget.blank) {
                                        imageInDiv(state.files[it.value.toInt()]?.src
                                            ?: "", "contain", 300.px, 200.px) {
                                            css.minWidth = 300.px
                                            css.marginRight = 20.px
                                            css.cursor = Cursor.pointer
                                        }
                                    }
                                }
                            }
                            styledDiv {
                                css {
                                    child("b") {
                                        fontWeight = FontWeight.bold
                                    }
                                }
                                p.forEach { field ->
                                    if (field.name !in listOf("agree", "know", "submit", "imageFileId")) {
                                        b {
                                            +field.title.let { if (it != "") it else field.name }
                                        }
                                        if (field.value != Unit.toString()) {
                                            b { +": " }
                                            +field.value
                                        }
                                        br { }
                                    }
                                }
                                p.find { it.name == "fileId" || it.name == "mediaFileId" }?.also { field ->
                                    if (state.files.containsKey(field.value.toInt())) {
                                        b { +"Ссылка на файл: " }
                                        state.files.getValue(field.value.toInt()).src.let {
                                            a(href = it, target = ATarget.blank) {
                                                +it
                                            }
                                        }
                                    } else {
                                        GlobalScope.launch {
                                            val file = Request.AdminGetFileById(field.value.toInt()).send().parseAnswerBody(FileAnswer.serializer())
                                            setState { files[field.value.toInt()] = file }
                                        }
                                    }
                                }
                            }
                        }
                    }
            } else {
                GlobalScope.launch {
                    val p = Request.AdminParticipantGetAll(formType)
                        .send().parseAnswerBody(InputField.serializer().list.list)
                    setState { this.participants = p }
                    if (formType == FormType.Art || formType == FormType.Photos) {
                        val images = Request.ParticipantsImagesGetAll(formType, 300 x 200 put OUTSIDE).send().parseAnswerBody(FileAnswer.serializer().list)
                        setState { this.files = images.associateBy { it.id }.toMutableMap() }
                    }
                }
            }
            goNext()
        }
    }

    override fun StyledDOMBuilder<*>.page() {
        if (state.isAdmin != undefined) {
            if (state.isAdmin) {
                sectionsAmount = 0
                section("Добавить новость:") {
                    styledDiv {
                        css {
                            backgroundColor = Color.white
                        }
                        child(Contest::class) {
                            attrs {
                                formType = FormType.NewsForm
                            }
                        }
                    }
                }
                section("Добавить раскраску:") {
                    styledDiv {
                        css {
                            backgroundColor = Color.white
                        }
                        child(Contest::class) {
                            attrs {
                                formType = FormType.RaskraskaForm
                            }
                        }
                    }
                }
                section("Добавить сказку:") {
                    styledDiv {
                        css {
                            backgroundColor = Color.white
                        }
                        child(Contest::class) {
                            attrs {
                                formType = FormType.StoriesForm
                            }
                        }
                    }
                }

//                forms("ЭтноТур", FormType.Art)
//                forms("Лики земли Карельской", FormType.Photos)
//                forms("Калевала — страна солнца", FormType.Music)
//                forms("ЭтноМотив", FormType.Dance)
//                forms("Научно-деловая программа", FormType.Scientific)
//                forms("Научно-деловая программа: Статьи", FormType.Literature)
//                forms("Организовать площадку этнофестиваля", FormType.Professional)

                listOf(FormType.Art, FormType.Photos, FormType.Music, FormType.Dance, FormType.Scientific, FormType.Literature, FormType.Professional).forEach {
                    forms(it.title, it)
                }

            } else {
                child(Contest::class) {
                    attrs.formType = FormType.Admin
                    attrs.action = {
                        setState {
                            isAdmin = it.answerType == AnswerType.OK
                        }
                    }
                }
            }
        } else {
            GlobalScope.launch {
                val r = Request.AdminCheck().send().parseAnswer().answerType == AnswerType.OK
                setState {
                    isAdmin = r
                }
            }
        }
    }
}