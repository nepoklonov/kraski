package kraski.server.database

import kraski.common.SaveToTable
import kraski.common.getDefault
import kraski.common.getSerializer
import kotlinx.serialization.Mapper
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.memberProperties

class ModelTable<T : Any>(val model: Model<T>) : Table(model.kClass.simpleName!!) {
    init {
        model.fields.forEach {
            when (it.type) {
                ModelFieldType.INT -> integer(it.name).apply { if (!it.nullable) default(Int.MIN_VALUE) }
                ModelFieldType.BOOLEAN -> bool(it.name).apply { if (!it.nullable) default(false) }
                ModelFieldType.STRING -> varchar(it.name, 2000).apply { if (!it.nullable) default("") }
                ModelFieldType.TEXT -> text(it.name).apply { if (!it.nullable) default("") }
                ModelFieldType.HIDDEN -> null
            }?.run {
                if (it.isPrimaryKey) primaryKey() else this
            }?.run {
                if (it.autoIncremented) autoIncrement() else this
            }?.run {
                if (it.nullable) nullable() else this
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun insert(modelInstance: T): InsertStatement<Number> {
        val fieldValues = model.toMap(modelInstance)
        return insert { op ->
            model.fields.forEach { field ->
                if (field.type != ModelFieldType.HIDDEN) {
                    val column = columns.first { it.name == field.name }
                    if (!field.autoIncremented) {
                        when (field.type) {
                            ModelFieldType.INT -> {
                                op[column as Column<Int>] = fieldValues.getValue(field.name).toInt()
                            }
                            ModelFieldType.BOOLEAN -> {
                                op[column as Column<Boolean>] = fieldValues.getValue(field.name).toBoolean()
                            }
                            ModelFieldType.STRING, ModelFieldType.TEXT -> {
                                op[column as Column<String>] = fieldValues.getValue(field.name)
                            }
                            else -> {
                            }
                        }
                    }
                }
            }
        }
    }

    fun getColumn(s: String) = columns.first { it.name == s }

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(s: String) = getColumn(s) as Column<T>

    operator fun <R> get(property: KProperty<R>): Column<R> {
        return get(property.name)
    }

    inline fun selectModels(where: SqlExpressionBuilder.(ModelTable<T>) -> Op<Boolean>): List<T> {
        return select {
            where(this@ModelTable)
        }.map { it.toModel() }
    }

    fun selectAllModels(): List<T> {
        return selectAll().map { it.toModel() }
    }

    fun selectAllModelsOrderBy(column: Expression<*>, order: SortOrder = SortOrder.ASC): List<T> {
        return selectAll().orderBy(column, order).map { it.toModel() }
    }

    fun create() = apply {
        SchemaUtils.createMissingTablesAndColumns(this)
    }

    fun ResultRow.toModel(): T {
        val map = model.fields.filter { it.type != ModelFieldType.HIDDEN }.associate {
            it.name to this[getColumn(it.name)]!!
        }
        val resultMap = model.kClass.memberProperties.map {
            it.name to if (it.findAnnotation<SaveToTable>(model.kClass) != null) {
                when (it.returnType.classifier) {
                    String::class, Int::class, Boolean::class, Unit::class -> map.getValue(it.name)
                    else -> (it.returnType.classifier as KClass<*>).run {
                        require(java.isEnum) { "unexpected property's type ${this.simpleName} in model class" }
                        java.enumConstants.indexOfFirst { e -> e.toString() == map.getValue(it.name).toString() }
                    }
                }
            } else (it.returnType.classifier as KClass<*>).getDefault()
        }.toMap()
        return Mapper.unmap(model.kClass.getSerializer(), resultMap)
    }
}

