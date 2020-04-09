package kraski.client.stucture

import kraski.client.PageClass
import kraski.client.RoutedProps
import kraski.client.stucture.header.HeaderComponent
import kotlinx.css.*
import kotlinx.html.id
import kraski.client.gridTemplateAreas
import react.RBuilder
import react.RComponent
import react.RState
import styled.css
import styled.styledDiv

interface RootProps : RoutedProps, PageProps {
    var pageComponent: PageClass
}

class RootComponent(props: RootProps) : RComponent<RootProps, RState>(props) {

    override fun RBuilder.render() {
        styledDiv {
            attrs.id = "root"
            css {
                width = 100.pct
                height = 100.pct
                display = Display.grid
                gridTemplateAreas(
                        ". . . info .",
                        "h-back h-back h-back h-back h-back",
                        ". ornament . header .",
                        ". left-nav . head-nav .",
                        ". left-nav . content .",
                        ". . . . .",
                        "footer footer footer footer footer"
                        )
                gridTemplateRows = GridTemplateRows("60px 0 140px 40px auto 40px auto")
                gridTemplateColumns = GridTemplateColumns("minmax(60px, 3fr) 160px minmax(40px, 2fr) minmax(600px, 4000px) minmax(60px, 3fr)")
            }
            child(HeaderComponent::class) {
                attrs.current = props.current
            }
            console.log(props.pageComponent)
            child(props.pageComponent) {
                attrs.current = props.current
                attrs.pageTitle = props.pageTitle
            }
        }
    }
}