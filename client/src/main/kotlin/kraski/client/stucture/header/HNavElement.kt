package kraski.client.stucture.header

import kraski.client.*
import kraski.client.elements.image.svgImage
import kraski.common.interpretation.getSectionIcon
import kraski.common.interpretation.x
import kotlinx.css.*
import kotlinx.html.CommonAttributeGroupFacade
import kotlinx.html.DIV
import kotlinx.html.classes
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.asList
import react.*
import react.router.dom.navLink
import styled.StyledDOMBuilder
import styled.css
import styled.styledDiv

interface HNavProps : RProps {
    var section: Section
    var current: String
    var content: StyledDOMBuilder<DIV>.() -> Unit
}

interface HNavState : RState {
    var selected: Boolean
    var links: List<Double>
    var sectionTopHeight: Double
}

class HNavElement : RComponent<HNavProps, HNavState>() {
    private val isMain
        get() = props.current == Section.Main.url
    private val shouldBeOpened
        get() = state.selected || isMain

    init {
        state.selected = false
        state.links = listOf()
        state.sectionTopHeight = 0.0
    }

    private fun StyledDOMBuilder<CommonAttributeGroupFacade>.addSelectListener() {
        attrs {
            onMouseEnterFunction = {
                setState { selected = true }
            }
            onMouseLeaveFunction = {
                setState { selected = false }
            }
        }
    }

    private fun RBuilder.divLink(color2: Color, to: String, title: String) = styledDiv {
        css {
            width = 100.pct
            fontSize = 16.px
            display = Display.flex
            alignItems = Align.center

        }
        styledDiv {
            css {
                position = Position.relative
                zIndex = 2
                margin(7.px, 0.px, 7.px, 25.px)
                children("a") {
                    +MainStyles.normalA
                }
                if (to == props.current) +MainStyles.current
            }
            navLink(to) {
                +title
            }

        }
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                marginBottom = 5.px
                justifySelf = JustifySelf.center
            }
            svgImage(getSectionIcon(props.section.self.ref.name), 100 x 70) {
                addSelectListener()
            }
        }
        styledDiv {
            ref { node ->
                if (node != undefined) {
                    (node as Element).getBoundingClientRect().height.let {
                        if (state.sectionTopHeight != it) setState { sectionTopHeight = it }
                    }
                }
            }
            addSelectListener()
            css {
                display = Display.flex
                position = Position.relative
                width = 100.pct
                display = Display.flex
                alignItems = Align.center
                justifyContent = JustifyContent.center
                textAlign = TextAlign.center
            }
            if (shouldBeOpened) {
                topLine(getPageColor(props.section.self.ref, true).toColor(), state.sectionTopHeight, state.links.isNotEmpty())
            }
            styledDiv {
                css {
                    width = 70.pct
                    fontWeight = FontWeight.bold
                    child("a") {
                        +MainStyles.normalA
                        fontWeight = FontWeight.bold
                    }
                    textAlign = TextAlign.center
                    if (props.current.startsWith(props.section.url)) +MainStyles.current
                }
                props.section.title.let {
                    navLink(props.section.url) { +it }
                }
            }
        }
        styledDiv {
            if (state.links.isNotEmpty() && shouldBeOpened)
                bottomLine(getPageColor(props.section.self.ref, true).toColor(), state.links)
            css {
                position = Position.relative
                if (!isMain) height = 0.px
            }
            styledDiv {
                attrs.classes += "h-" + props.section.self.ref.name
                if (shouldBeOpened) {
                    addSelectListener()
                    css {
                        if (isMain) height = 100.pct
                        paddingRight = 5.px
                        backgroundColor = getPageColor(props.section.self.ref, false).toColor()
                    }
                    if (props.section.pages.isNotEmpty()) {
                        ref { node ->
                            if (node != undefined) {
                                val links = (node as Node).childNodes.asList().map {
                                    (it as Element).getBoundingClientRect().run { top + height / 2 } -
                                        (node as Element).getBoundingClientRect().top
                                }
                                if (state.links != links) setState {
                                    this.links = links
                                }
                            }
                        }
                    }
                    props.run { content() }
                    props.section.pages.forEach {
                        divLink(getPageColor(props.section.self.ref, true).toColor(), it.url, it.title)
                    }
                }
            }
        }
    }
}
