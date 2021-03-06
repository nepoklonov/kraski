package kraski.client.pages

import kotlinx.serialization.list
import kotlinx.serialization.serializer
import kraski.client.send
import kraski.client.stucture.YamlListState
import kraski.client.stucture.initYamlListState
import kraski.client.stucture.updateYamlListState
import kraski.common.Request
import kraski.common.models.participants.FormType
import react.RBuilder
import react.RComponent
import react.RProps
import react.dom.br
import react.dom.h3

interface AntiProps : RProps {
    var formType: FormType
}

class AntiGallery : RComponent<AntiProps, YamlListState<String>>() {
    init {
        initYamlListState()
    }

    override fun RBuilder.render() {
        if (state.yaml.isEmpty()) {
            Request.ParticipantsDataGetAll(props.formType).send(String.serializer().list, ::updateYamlListState)
        }
        h3 {
            +"Список участников: "
        }
        state.yaml.forEachIndexed { index, s ->
            val i = index + 1
            +"$i. $s"
            br {}
        }
    }
}