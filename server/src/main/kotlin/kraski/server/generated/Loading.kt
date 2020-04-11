package kraski.server.generated

import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import kraski.server.Level
import kraski.server.UserSession
import kotlinx.html.*

fun Route.generateLoadingHTML(path: String) {
    get(path) {
        if (call.sessions.get<UserSession>() == null) {
            call.sessions.set(UserSession(Level.JustSomeone))
        }
        call.respondHtml {
            head {
                link(rel = "stylesheet", href = "/styles.css", type = "text/css")
                link(rel = "icon", href = "/images/design/favicon.ico", type = "image/x-icon")
            }
            body("wrapper") {
                div("wrapper") {
                    id = "js-response"
                    div("wrapper wrapper-loading") {
                        div("js-loading") {
                            +"Loading..."
                        }
                    }
                }
//                script(src="/inwidget/index.php") {}
                script(src="https://www.facebook.com/plugins/page.php?href=https%3A%2F%2Fwww.facebook.com%2Ffacebook&tabs=timeline&width=280&height=280&small_header=false&adapt_container_width=true&hide_cover=false&show_facepile=true&appId=1198269693659049"){}
                script(src="https://vk.com/js/api/openapi.js?167"){}
                script(src = "/main.bundle.js") {}
            }
        }
    }
}