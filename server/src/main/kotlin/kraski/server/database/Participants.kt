package kraski.server.database

import kraski.common.models.participants.AnyForm
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
fun addForm(form: AnyForm, klass: KClass<out AnyForm>): String {
    var ok = ""
    transaction {
        addLogger(StdOutSqlLogger)
        val participantTable = klass.getModelTable()
        (participantTable as ModelTable<AnyForm>).insert(form)
        ok = "ok"
    }
    return ok
}