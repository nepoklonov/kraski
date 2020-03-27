package kraski.client.pages

import kotlinx.css.*
import kotlinx.serialization.list
import react.dom.a
import react.dom.p
import kraski.client.gray50Color
import kraski.client.send
import kraski.client.stucture.*
import kraski.common.Request
import kraski.common.interpretation.JsonRef
import kraski.common.Resource
import kraski.common.interpretation.ImageDirs
import styled.*

class ResourcesComponent(props: PageProps) : StandardPageComponent<YamlListState<Resource>>(props) {
    init {
        initYamlListState()
        Request.GetJson(JsonRef.ResourcesJson).send(Resource.serializer().list, ::updateYamlListState)
    }

    override fun StyledDOMBuilder<*>.page() {
            state.yaml.forEach {
                styledDiv {
                    css {
                        display = Display.flex
                        margin(25.px, 30.px, 25.px, 20.px)
                    }
                    a(href = it.link, target = "_blank") {
                        styledImg(src = (ImageDirs.resources file it.logo).path) {
                            css {
                                maxWidth = 80.px
                                maxHeight = 80.px
                            }
                        }
                    }
                    styledDiv {
                        css {
                            display = Display.flex
                            flexDirection = FlexDirection.column
                            justifyContent = JustifyContent.flexStart
                            paddingLeft = 10.px
                        }
                        p {
                            +it.name
                        }
                        styledP {
                            css {
                                marginTop = 5.px
                                color = gray50Color
                                fontSize = 10.pt
                            }
                            +it.text
                        }
                    }
                }
            }
        }
}