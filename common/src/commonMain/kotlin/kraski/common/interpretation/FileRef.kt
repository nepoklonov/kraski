package kraski.common.interpretation

import kraski.common.div
import kraski.common.interpretation.DirRef.Companion.documents
import kraski.common.interpretation.DirRef.Companion.images
import kraski.common.interpretation.DirRef.Companion.root
import kraski.common.interpretation.ScaleType.INSIDE
import kraski.common.interpretation.ScaleType.OUTSIDE
import kraski.common.models.participants.FormType
import kotlinx.serialization.Serializable

@Serializable
data class DirRef constructor(val parent: DirRef? = null, val name: String) {
    val path: String
        get() = parent?.let { it.path / name } ?: "/$name"
    val tree: List<String>
        get() = if (parent == null) listOf(name) else parent.tree + name

    companion object {
        val root = DirRef(null, "")
        val json = root / "json"
        val images = root / "images"
        val documents = root / "documents"
        operator fun invoke(refString: String): DirRef {
            var dirRef = root
            refString.split('/').forEach { if (it != "") dirRef /= it }
            return dirRef
        }
    }

    operator fun div(other: String) = DirRef(this.takeIf { it != root }, other)
    infix fun file(other: String) = FileRef(this, other)
}

infix fun DirRef.from(other: DirRef): String {
    var i = 0
    val oneTree = this.tree
    val otherTree = other.tree
    while (i < oneTree.size && i < otherTree.size && oneTree[i] == otherTree[i]) i++
    var result: String = ""
    for (j in i until otherTree.size) {
        result += "../"
    }
    for (j in i until oneTree.size) {
        result += oneTree[j]
    }
    return result.trim('/')
}

@Serializable
data class FileRef(val dir: DirRef, val fileName: String) {
    val path: String
        get() = dir.path / fileName

    companion object {
        operator fun invoke(refString: String): FileRef {
            var dirRef = root
            val strTree = refString.split('/').filter { it != "" }
            strTree.forEachIndexed { index, s ->
                if (index != strTree.size - 1) dirRef /= s
            }
            return dirRef file strTree.last()
        }
    }
}

infix fun FileRef.dot(ext: String) = copy(fileName = "$fileName.$ext")

object Documents {

    fun getOfficialPDF(current: String): String {
        return (documents file (DirRef(current) from Pages.join) dot "pdf").path
    }

    fun getOfficialPDF(formType: FormType): String {
        return (documents file formType.folder.name dot "pdf").path
    }

    val ustav = documents file "ustav.pdf"
    val conceptionPDF = documents file "conception.pdf"

    val cwForm = documents file "cw-form.pdf"
    val cwApplication = documents file "cw-application.jpg"
}


object ImageDirs {
    val design = images / "design"
    val contacts = images / "contacts"
    val partners = images / "partners"
    val resources = images / "resources"
    val chuvashia = images / "chuvashia"
    val smi = images / "smi"
    val symbols = images / "symbols"
    val team = images / "team"
    val history = images / "history"
    val old = images / "old"
    val people = images / "people"
    val svg = images / "svg"
    val join = images / "join"
    val logos = images / "logos"
    val commonwealth = images / "commonwealth"

    fun getPoster(current: String): String? {
        return if (current == Pages.Join.organize.path) null
        else (join file (DirRef(current) from Pages.join) dot "jpg").path
    }

    fun getImageFromDirectory(dir: DirRef, fileRef: FileRef): Image<*> {
        require(fileRef.dir.path.startsWith(dir.path)) { "$fileRef is not in $dir" }
        return when (dir) {
            old -> Image(fileRef, getDirectoryScales(dir))
            else -> TODO()
        }
    }

    fun getDirectoryScales(dir: DirRef) = when (dir) {
        old -> 250 x 125 put OUTSIDE
        else -> TODO()
    }
}

//enum class Category(title: String) {
//    Images("images"),
//    Sections("sections"),
//    Old("old"),
//}

object Images {
}

object Pages {
    val main = root
    val commonwealth = root / "commonwealth"
    val about = root / "about"
    val contacts = root / "contacts"
    val news = root / "news"
    val raskraska = root / "raskraska"
    val stories = root / "stories"
    val partners = root / "partners"
    val history = root / "history"
    val team = root / "team"

    object About {
        val photos = about / "photos"
    }

    val join = root / "join"

    object Join {
        val organize = join / "organize"

        val art = join / "art"
        val photos = join / "photos"
        val music = join / "music"
        val dance = join / "dance"
        val professional = join / "professional"
        val scientific = join / "scientific"
        val literature = join / "literature"
    }

    val gallery = root / "gallery"

    object Gallery {
        val art = gallery / "art"
        val photos = gallery / "photos"
        val music = gallery / "music"
        val dance = gallery / "dance"
        val professional = gallery / "professional"
        val scientific = gallery / "scientific"
        val literature = gallery / "literature"
    }

    val chuvashia = root / "chuvashia"

    object Chuvashia {
        val literature = chuvashia / "literature"
        val archives = chuvashia / "archives"
        val symbols = chuvashia / "symbols"
        val palaces = chuvashia / "palaces"
        val libraries = chuvashia / "libraries"
        val theatres = chuvashia / "theatres"
        val museums = chuvashia / "museums"

    }

    val uploads = root / "uploads"

    object Uploads {
        val temp = uploads / "temp"
        val images = uploads / "images"

        object Images {
            val art = images / "art"
            val photos = images / "photos"
            val news = images / "news"
            val raskraska = images / "raskraska"
            val stories = images / "stories"
        }

        val other = uploads / "other"

        object Other {
            val music = other / "music"
            val dance = other / "dance"
            val professional = other / "professional"
            val scientific = other / "scientific"
            val literature = other / "literature"
        }
    }

    val admin = root / "admin"
}