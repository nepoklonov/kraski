package kraski.common.models

import kraski.common.SaveToTable
import kraski.common.interpretation.DirRef
import kraski.common.models.participants.FormType
import kotlinx.serialization.Serializable

@Serializable
class ParticipantFile (
    @SaveToTable(isPrimaryKey = true, autoIncremented = true)
    val id: Int,

    @SaveToTable
    val formType: FormType,

    @SaveToTable
    val name: String,

    @SaveToTable
    val oldName: String
) {
    val fileRef
        get() = formType.folder file name
}

@Serializable
data class StaticFile (
    @SaveToTable
    val category: String,

    @SaveToTable
    val dirPath: String,

    @SaveToTable
    val fileName: String,

    @SaveToTable
    val width: Int,

    @SaveToTable
    val height: Int
) {
    @SaveToTable(isPrimaryKey = true, autoIncremented = true)
    val id: Int = -1

    val fileRef
        get() = DirRef(dirPath) file fileName

    val copiesDirRef
        get() = DirRef(dirPath) / "copies" / fileName.substringBeforeLast(".")
}