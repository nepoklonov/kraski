package kraski.client.pages.chuvashia

import kraski.client.indentedDiv
import kraski.client.send
import kraski.client.stucture.*
import kraski.common.interpretation.JsonRef
import kotlinx.serialization.list
import kraski.common.*
import react.dom.a
import react.dom.b
import react.dom.br
import react.dom.hr
import styled.StyledDOMBuilder
import styled.styledLi
import styled.styledUl

class Libraries(props: PageProps) : StandardPageComponent<YamlListState<LibrariesJSON>>(props) {
    init {
        initYamlListState()
        Request.GetJson(JsonRef.ChuvashiaLibrariesJson).send(LibrariesJSON.serializer().list, ::updateYamlListState)
    }

    override fun StyledDOMBuilder<*>.page() {
        styledUl {
            state.yaml.forEach {
                styledLi {
                    a(href = it.link) {
                        +it.name
                    }
                }
            }
        }
    }
}
