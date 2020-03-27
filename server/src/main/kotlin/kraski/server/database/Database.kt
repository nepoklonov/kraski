package kraski.server.database

import kraski.common.Titled
import kraski.common.models.ImageVersion
import kraski.common.models.ParticipantFile
import kraski.common.models.StaticFile
import kraski.common.models.StaticImageVersion
import kraski.common.models.participants.FormType
import kraski.common.models.participants.NotOrganizer
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.full.isSubclassOf

val tables: MutableMap<KClass<*>, ModelTable<*>> = mutableMapOf()

fun startDB() {
    Database.connect("jdbc:h2:file:./data/main", driver = "org.h2.Driver")
    val tableNames = mutableListOf<String>()
    transaction {
        (FormType.values().map {
            it.klass
        } + listOf(
            ParticipantFile::class,
            StaticFile::class,
            ImageVersion::class,
            StaticImageVersion::class)).forEach {
            it.getModelTable()
            tableNames += it.simpleName!!
        }
        println("DB started with tables: $tableNames")
    }
}

fun getTitles(klass: KClass<*>): List<String> {
    require(klass.java.isEnum && klass.isSubclassOf(Titled::class))
    return klass.java.enumConstants.map { (it as Titled).title }
}

fun getTitles(klass: KClassifier?): List<String> = getTitles(klass as KClass<*>)

fun <T> loggedTransaction(statement: Transaction.() -> T) = transaction {
    addLogger(StdOutSqlLogger)
    statement()
}

@Suppress("UNCHECKED_CAST")
fun <T : Any> KClass<T>.getModelTable(): ModelTable<T> =
    if (this in tables) tables[this] as ModelTable<T> else {
        ModelTable(createModel()).create().also {
            tables[this] = it
        }
    }

fun getAmount(formType: FormType): Int {
    return transaction {
        val participantTable = formType.klass.getModelTable()
        participantTable.selectAll().count()
    }
}

@Suppress("UNCHECKED_CAST")
fun getCities(formType: FormType): Set<String> {
    return transaction {
        if (formType.klass.isSubclassOf(NotOrganizer::class)) {
            val participantTable = (formType.klass as KClass<NotOrganizer>).getModelTable()
            participantTable.selectAll().map { it[participantTable.get<String>(NotOrganizer::city.name)] }.toSet()
        } else setOf()
    }
}