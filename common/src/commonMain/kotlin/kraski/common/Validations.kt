package kraski.common

fun getValidatorByPattern (r: Regex) : (String) -> Boolean =
    Regex(r.pattern, RegexOption.IGNORE_CASE)::matches

enum class Validation(val validate: (String) -> Boolean) {
    Any({ true }),
    FilledCheckBox({ it == "true" }),
    AnyCheckBox({ it == "true" || it == "false" }),
    Email(getValidatorByPattern(".+@.+\\..+".toRegex())),
    Text(getValidatorByPattern(".{1,1999}".toRegex())),
    LongText(getValidatorByPattern(".+".toRegex())),
    Number(getValidatorByPattern("[1-9]\\d?".toRegex())),
    ImageFileName(getValidatorByPattern(".+\\.(jpg|jpe|jpeg|tiff|webp|png|bmp|gif)".toRegex())),
    Select(getValidatorByPattern("\\d+".toRegex())),
    Radio(getValidatorByPattern("\\d+".toRegex())),
    EssayFileName(getValidatorByPattern(".+".toRegex())),
    Date(getValidatorByPattern("\\d{2}\\.\\d{2}\\.\\d{4}".toRegex()))
}