package kraski.client.pages.chuvashia

import kotlinx.serialization.list
import kraski.client.send
import kraski.client.stucture.*
import kraski.common.Request
import kraski.common.TheatresJSON
import kraski.common.interpretation.JsonRef
import react.dom.a
import react.dom.b
import react.dom.br
import styled.StyledDOMBuilder
import styled.styledLi
import styled.styledUl

class Theatres(props: PageProps) : StandardPageComponent<YamlListState<TheatresJSON>>(props) {
    init {
        initYamlListState()
        Request.GetJson(JsonRef.ChuvashiaTheatresJson).send(TheatresJSON.serializer().list, ::updateYamlListState)
    }

    override fun StyledDOMBuilder<*>.page() {
        styledUl {
            state.yaml.forEach {
                styledLi {
                    a(href = it.link) {
                        +it.name
                    }
                    br { }
                    b {
                        +"Адрес: "
                    }
                    a(href=it.addressLink) {
                        +it.address
                    }
                }
            }
        }
    }
}
