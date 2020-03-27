package kraski.client.pages.chuvashia

import kraski.client.send
import kraski.client.stucture.*
import kraski.common.KareliaHistory
import kraski.common.Request
import kraski.common.interpretation.JsonRef
import kotlinx.css.*
import kotlinx.html.ATarget.blank
import kotlinx.serialization.list
import kraski.common.ArchiveJSON
import react.dom.p
import styled.*

class Archives(props: PageProps) : StandardPageComponent<YamlListState<ArchiveJSON>>(props) {
    init {
        initYamlListState()
        Request.GetJson(JsonRef.ChuvashiaArchiveJson).send(ArchiveJSON.serializer().list, ::updateYamlListState)
    }

    override fun StyledDOMBuilder<*>.page() {
        styledUl {
            state.yaml.forEach { archive ->
                styledLi {
                    styledA(href = archive.link, target = blank) {
                        +archive.name
                    }
                }
            }
        }
    }
}
