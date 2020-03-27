package kraski.common

import kotlinx.serialization.Serializable

@Serializable
data class Resource(val logo: String, val link: String, val name: String, val text: String)

@Serializable
data class TeamPerson(
        val position: Int,
        val fullName: String,
        val picture: String,
        val description: String,
        val email: String
)

@Serializable
data class Team(val positions: List<String>, val team: List<TeamPerson>)

@Serializable
data class Contact(val name: String, val logo: String, val link: String, val text: String)

@Serializable
data class Partner(val name: String, val logo: String, val link: String)

@Serializable
data class Partners(val partners: List<Partner>, val orgs: List<Partner>)

@Serializable
data class Bio(val name: String, val date: String, val text: String)

@Serializable
data class JoinJSON(val name: String, val portrait: String, val icon: String, val bio: Bio)

@Serializable
data class ChuvashiaSection(
        val name: String,
        val title: String,
        val logo: String
)

@Serializable
data class Song(val name: String, val song: String)

@Serializable
data class SymbolJSON(
        val header: String,
        val picture: String,
        val text: String,
        val songs: List<Song>
)

@Serializable
data class ArchiveJSON(val name: String, val link: String)

@Serializable
data class PalacesJSON(val link: String, val name: String, val comment: String, val address: String)

@Serializable
data class KareliaHistory(val header: String, val text: String, val source: String)

@Serializable
data class LiteratureItem(val bold: String, val light: String, val link: String)

@Serializable
data class LiteratureJSON(val header: String, val subheader: String, val content: List<LiteratureItem>)

@Serializable
data class LibrariesJSON(val link: String, val name: String)

@Serializable
class TheatresJSON(val link: String, val name: String, val addressLink: String, val address: String)

@Serializable
data class MuseumJSON(val link: String, val name: String, val comment: String, val address: String)
