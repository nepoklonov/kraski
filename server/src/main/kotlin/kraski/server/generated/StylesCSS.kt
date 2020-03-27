package kraski.server.generated

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import kotlinx.css.*
import kotlinx.css.properties.LineHeight
import kotlinx.css.properties.s
import kotlinx.css.properties.transition
import kraski.common.General

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}

fun Route.generateStylesCSS(path: String) {
    get(path) {
        call.respondCss {
            rule("@font-face") {
                fontFamily = "Lato"
                put("src", "url('/fonts/Lato-Medium.ttf')")
//                    "url('/fonts/kalevala.woff') format('woff'), " +
//                    "url('/fonts/kalevala.svg') format('svg')")
            }
            rule("*") {
                transition(duration = 0.3.s)
                fontFamily = "Lato"
                fontWeight = FontWeight.normal
                margin = "0"
                padding = "0"
            }
            body {
                fontSize = General.defaultFontSize.px
            }
            a {
                color = Color("#6363bd")
            }
            rule("a:hover") {
                color = Color("#393973")
            }
            rule("a:active") {
                color = Color("#1c1c39")
            }
            rule(".wrapper") {
                width = 100.pct
                height = 100.pct
                display = Display.flex
                justifyContent = JustifyContent.center
            }
            rule(".wrapper-loading") {
                alignItems = Align.center
            }
            rule(".js-loading") {
                display = Display.flex
                color = Color("#008899")
            }
            rule("#js-response") {
                position = Position.absolute
                width = 100.pct
            }
        }
    }
}