@file:Suppress("UNCHECKED_CAST")

package kraski.server.api

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import io.ktor.util.pipeline.PipelineContext
import kraski.common.Answer
import kraski.common.FileAnswer
import kraski.common.Method
import kraski.common.Request
import kraski.common.models.AdminLogin
import kraski.common.models.InputField
import kraski.common.models.ParticipantFile
import kraski.common.models.participants.FormType
import kraski.server.Level
import kraski.server.UserSession
import kraski.server.database.*
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.serialization.list
import kraski.server.database.getModelTable
import kraski.server.database.loggedTransaction
import org.jetbrains.exposed.sql.selectAll
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

fun Route.startAdminAPI() {
    startAdminParticipantGetAllAPI()
    startAdminGetFileByIdAPI()
    startAdminCheckAPI()
    startTasksAPI()
}

fun Route.startAdminCheckAPI() = adminListenAndAutoRespond<Request.AdminCheck>(Method.AdminCheck) { request, _ ->
    Answer.ok()
}

fun PipelineContext<Unit, ApplicationCall>.adminLogin(adminLogin: AdminLogin): Answer {
    val levelFromPassword = Level.values().find { it.password == adminLogin.password } ?: Level.JustSomeone
    println(levelFromPassword)
    val userSession = call.sessions.get<UserSession>() ?: UserSession(Level.JustSomeone)
    call.sessions.set(userSession.copy(level = levelFromPassword))
    return if (levelFromPassword hasRights Level.Moderator) {
        Answer.ok()
    } else {
        Answer.wrong()
    }
}

fun Route.startAdminParticipantGetAllAPI() = adminListenAndAutoRespond<Request.AdminParticipantGetAll>(Method.AdminParticipantsGetAll) { request, _ ->
    val klass = request.formType.klass as KClass<Any>
    val model = klass.createModel()

    val v = klass.getModelTable().run {
        val participants = loggedTransaction {
            selectAll().map { it.toModel() }
        }
        participants.map { participant ->
            val list = model.fields.map { f ->
                val field = f.inputField.copy()
                field.value = klass.memberProperties.find { it.name == f.inputField.name }!!.get(participant).toString()
                field
            }.toMutableList()
            list.sortBy { it.order }
            println(list.map { it.value }.toString())
            list
        }
    }
    println(v.map { it.map { it.value } }.toString())
    Answer.ok(InputField.serializer().list.list, v)
}

fun Route.startAdminGetFileByIdAPI() = adminListenAndAutoRespond<Request.AdminGetFileById>(Method.AdminGetFileById) { request, _ ->
    val ref = ParticipantFile::class.getModelTable().let {
        loggedTransaction {
            it.selectModels { it[ParticipantFile::id] eq request.id }.first().fileRef
        }
    }
    Answer.ok(FileAnswer.serializer(), FileAnswer(request.id, ref.path))
}

fun Route.startTasksAPI() {
    get("/admin/tasks/!0") {
        call.respondHtml {
            body {
                if (isAdmin()) {
                    h1 {
                        +"Welcome to the Task Center"
                    }
                    loggedTransaction {
                        FormType.values().map {
                            it.klass.getModelTable()
                        }.map { table ->
                            table.model.fields.forEach { field ->
                                if (field.type == ModelFieldType.STRING || field.type == ModelFieldType.TEXT) {
                                    this.exec(table.getColumn(field.name).modifyStatement()[0])
                                }
                            }
                        }
                    }
                }
                h2 {
                    +"[Done]"
                }
            }
        }
    }
}
