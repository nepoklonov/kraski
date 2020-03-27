package kraski.server.database

import kraski.common.*
import kraski.common.models.InputField
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.superclasses


enum class ModelFieldType {
    INT, BOOLEAN, STRING, TEXT, HIDDEN
}

class ModelFieldData(
    val name: String,
    val inputField: InputField,
    val type: ModelFieldType,
    val nullable: Boolean,
    val isPrimaryKey: Boolean,
    val autoIncremented: Boolean
)

class Model<T : Any>(val fields: List<ModelFieldData>, val kClass: KClass<T>)

fun <T : Any> Model<T>.toMap(instance: T): Map<String, String> {
    return fields.associate { field ->
        field.name to kClass.memberProperties.first {
            it.name == field.name
        }.get(instance).toString()
    }
}


fun <T : Any> KClass<T>.createModel() = Model(this.memberProperties.asSequence().mapNotNull { prop ->
        val save = prop.findAnnotation<SaveToTable>(this)
        val display = prop.findAnnotation<Display>(this)
        val own = prop.findAnnotation<Belongs>(this)
        if (save != null || display != null) {
            val inputField = if (display != null) InputField(
                display.order,
                prop.name,
                display.title,
                display.validation,
                display.displayType,
                own?.ownType ?: OwnType.None,
                own?.ownParams?.toList() ?: listOf(),
                display.width,
                if (display.displayType.hasOptions) getTitles(prop.returnType.classifier) else listOf()
            ) else InputField(
                -1, prop.name, "",
                save?.forceValidation ?: Validation.Any,
                DisplayType.Hidden
            )

            val type = if (save != null) when (prop.returnType.classifier) {
                Int::class -> ModelFieldType.INT
                String::class -> if (save.longText) ModelFieldType.TEXT else ModelFieldType.STRING
                Boolean::class -> ModelFieldType.BOOLEAN
                else -> ModelFieldType.TEXT
            } else ModelFieldType.HIDDEN

            ModelFieldData(prop.name, inputField, type, save?.nullable ?: false,
                save?.isPrimaryKey ?: false, save?.autoIncremented ?: false)
        } else null
    }.toList(), this)

inline fun <reified R : Annotation> KProperty1<*, *>.findAnnotation(kClass: KClass<*>): R? {
    return findAnnotation<R>() ?: run {
        kClass.superclasses.flatMap { it.memberProperties }.filter {
            it.name == name
        }.mapNotNull { it.findAnnotation<R>() }.firstOrNull()
    }
}
