package kraski.server.api.join

import io.ktor.routing.Route
import kraski.common.*
import kraski.common.models.AdminLogin
import kraski.common.models.InputField
import kraski.common.models.participants.AnyForm
import kraski.common.models.participants.FormType
import kraski.common.models.participants.Participant
import kraski.server.api.adminLogin
import kraski.server.api.isAdmin
import kraski.server.api.listen
import kraski.server.api.respond
import kraski.server.database.addForm
import kraski.server.database.getModelTable
import kraski.server.database.loggedTransaction
import kotlinx.serialization.Mapper
import kraski.common.Answer
import kraski.common.Method
import kraski.common.Request
import kraski.common.getSerializer
import kraski.server.sendCertificate
import org.jetbrains.exposed.sql.deleteWhere
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

private fun InputField.isExpected(allFields: Map<String, InputField>) = ownType.live(ownParams, allFields)
private fun InputField.isValid() = validation.validate(value)
private fun InputField.isValidIfExpected(allFields: Map<String, InputField>) = isValid() || !isExpected(allFields)

fun parseForm(allFields: List<InputField>, formType: FormType, id: Int? = null): AnyForm? {
    return if (allFields.all { field -> field.isValidIfExpected(allFields.map { it.name to it }.toMap()) }) {
        val formKlass = formType.klass
        val map = allFields.map { it.name to it.value }.toMap().toMutableMap()
        map["id"] = id?.toString() ?: "0"
        if (formType == FormType.NewsForm) {
            map["date"] = map["date"]!!.split('.').reversed().joinToString(".") { it }
        }
        val resultMap = formKlass.memberProperties.asSequence().map {
            it.name to when (it.returnType.classifier) {
                String::class -> map[it.name] ?: getDefault<String>()
                Int::class -> map[it.name].run { if (this == "" || this == null) getDefault<Int>() else toInt() }
                Boolean::class -> map[it.name]?.toBoolean() ?: getDefault<Boolean>()
                Unit::class -> Unit
                else -> (it.returnType.classifier as KClass<*>).run {
                    if (java.isEnum) map.getValue(it.name).toInt() else Unit
                }
            }
        }.toMap()
        println(resultMap)
        Mapper.unmap(formKlass.getSerializer(), resultMap)
    } else null
}

fun Route.startFormSendAPI() = listen<Request.FormSend>(Method.FormSend) { request, _ ->
    val allFields = request.list
    val m = parseForm(allFields, request.formType, request.id)
    if (m != null) {
        if (request.id != null && isAdmin()) {
            loggedTransaction {
                m::class.getModelTable().deleteWhere { m::class.getModelTable()[AnyForm::id] eq request.id!! }
            }
        }
        if (request.formType == FormType.Admin) {
            respond(adminLogin(m as AdminLogin))
        } else {
            if (addForm(m, m::class) == "ok") {
                respond(Answer.ok("ok"))
                print(m.id)
                print(m.time)
                if (m is Participant) sendCertificate(request.formType, m, m::class)
            } else {
                respond(Answer.wrong("wtf"))
            }
        }
    } else {
        respond(Answer.wrong("something is wrong"))
    }
    //TODO check that file was uploaded
}