package kraski.client.pages

import kraski.client.send
import kraski.common.Partner
import kraski.common.Partners
import kraski.common.Request
import kraski.common.interpretation.ImageDirs
import kraski.common.interpretation.JsonRef
import kotlinx.css.*
import kotlinx.serialization.list
import kraski.client.stucture.*
import react.RBuilder
import react.dom.a
import react.dom.h2
import styled.StyledDOMBuilder
import styled.css
import styled.styledDiv
import styled.styledImg

//fun RBuilder.logos(partners: List<Partner>, logosHeight: LinearDimension, topMargin: LinearDimension) {
//    styledDiv {
//        css {
//            margin(topMargin, 0.px)
//            display = Display.flex
//            flexWrap = FlexWrap.wrap
//            justifyContent = JustifyContent.spaceAround
//        }
//        partners.forEach { partner ->
//            styledDiv {
//                css {
//                    display = Display.flex
//                    flexWrap = FlexWrap.wrap
//                    justifyContent = JustifyContent.center
//                    alignContent = Align.center
//                    alignItems = Align.center
//                    textAlign = TextAlign.center
//                    width = 27.pct
//                    margin(10.px, 0.px, 50.px, 0.px)
//                }
//                styledImg(src = (ImageDirs.history file partner.logo).path) {
//                    css {
//                        maxWidth = 40.pct
//                        minWidth = 100.px
//                        margin(0.px, 0.px, 20.px, 0.px)
//                    }
//                }
//                a(href = partner.link, target = "_blank") {
//                    +partner.name
//                }
//            }
//        }
//    }
//}

class HistoryComponent(props: PageProps) : StandardPageComponent<YamlListState<Partner>>(props) {
    init {
        initYamlListState()
        Request.GetJson(JsonRef.HistoryJson).send(Partner.serializer().list, ::updateYamlListState)
    }

    override fun StyledDOMBuilder<*>.page() {
        styledDiv {
            css {
                textAlign = TextAlign.left
                alignSelf = Align.flexEnd
            }
            +"Виртуальные выставки из фондов Чувашского национального музея и Государственного исторического архива Чувашской Республики"
        }
        logos(state.yaml, 100.px, 100.px)
    }
}