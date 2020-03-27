package kraski.common

import kraski.common.models.InputField
import kotlin.annotation.AnnotationTarget.PROPERTY

enum class DisplayType(val type: String) {
    Hidden("hidden"),
    Text("text"),
    HTML("text"),
    File("file"),
    CheckBox("checkbox"),
    Select("select"),
    Submit("submit"),
    Label("label"),
//    Multi("multi"),
    Radio("radio");
    val hasOptions
        get() = this == Select || this == Radio
}

enum class OwnType(val live: (List<String>, Map<String, InputField>) -> Boolean) {
    None({_, _ -> true}),
    ByCheckBox({params, map -> map[params[0]]?.value == "true"}),
    BySelect({params, map -> map[params[0]]?.value == params[1]})
}

@Target(PROPERTY)
annotation class SaveToTable(
    val nullable: Boolean = false,
    val autoIncremented: Boolean = false,
    val isPrimaryKey: Boolean = false,
    val longText: Boolean = false,
    val forceValidation: Validation = Validation.Any
)

@Target(PROPERTY)
annotation class Display(
        val order: Int,
        val title: String,
        val displayType: DisplayType = DisplayType.Text,
        val validation: Validation = Validation.Text,
        val width: Int = 100
)

@Target(PROPERTY)
annotation class Belongs(
        val ownType: OwnType,
        val ownParams: Array<String>
)

interface Titled {
    val title: String
}