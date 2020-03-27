package kraski.client.pages

import kotlinx.css.*
import kotlinx.css.properties.borderTop
import kraski.client.gray50Color
import kraski.client.gray70Color
import react.dom.h3
import react.dom.p
import kraski.common.Request
import kraski.common.interpretation.JsonRef
import kraski.client.send
import kraski.client.stucture.PageProps
import kraski.client.stucture.StandardPageComponent
import kraski.client.stucture.YamlState
import kraski.client.stucture.updateYamlState
import kraski.common.Team
import kraski.common.interpretation.ImageDirs
import styled.*

class TeamComponent(props: PageProps) : StandardPageComponent<YamlState<Team>>(props) {
    init {
        state.yaml = Team(emptyList(), emptyList())
        Request.GetJson(JsonRef.TeamJson).send(Team.serializer(), ::updateYamlState)
    }

    override fun StyledDOMBuilder<*>.page() {
        css {
            width = 100.pct
            height = 100.pct
        }
        for (i in state.yaml.positions.indices) {
            styledDiv {
                css {
                    width = 100.pct
                    margin(10.px, 10.pct, 30.px, 0.px)
                    borderTop(1.px, BorderStyle.solid, gray50Color)
                }
                styledH3 {
                    css {
                        color = gray70Color
                        margin(0.px, 40.px)
                        position = Position.relative
                        top = (-16).px
                    }
                    styledSpan {
                        css {
                            backgroundColor = Color.white
                            padding(0.px, 10.px)
                            fontSize = 18.px
                        }
                        +state.yaml.positions[i]
                    }
                }
                state.yaml.team.forEach {
                    if (it.position == i + 1) {
                        styledDiv {
                            css {
                                margin(10.px, 0.px)
                                display = Display.flex
                            }
                            styledImg(src = (ImageDirs.team file it.picture).path) {
                                css {
                                    marginRight = 10.px
                                    maxWidth = 120.px
                                    maxHeight = 300.px
                                }
                            }
                            styledDiv {
                                p {
                                    +it.fullName
                                }
                                styledP {
                                    css {
                                        fontSize = 10.pt
                                        color = Color.darkGray
                                    }
                                    p {
                                        +it.description
                                    }
                                    p {
                                        +it.email
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}