package kraski.common.interpretation

import kotlinx.serialization.KSerializer
import kotlinx.serialization.list
import kraski.common.*


enum class JsonRef(private val jsonName: String, val serializer: KSerializer<out Any>) {
    ResourcesJson("resources", Resource.serializer().list),
    JoinJson("konkurs", JoinJSON.serializer().list),
    TeamJson("team", Team.serializer()),
    ContactsJson("contacts", Contact.serializer().list),
    PartnersJson("partners", Partner.serializer().list),
    HistoryJson("history", Partner.serializer().list),
    LogosJson("logos", Partner.serializer().list),
    ChuvashiaSectionsJson("chuvashia/sections", ChuvashiaSection.serializer().list),
    ChuvashiaLiteratureJson("chuvashia/literature", LiteratureJSON.serializer().list),
    ChuvashiaArchiveJson("chuvashia/archives", ArchiveJSON.serializer().list),
    ChuvashiaSymbolsJson("chuvashia/symbols", SymbolJSON.serializer().list),
    ChuvashiaPalacesJson("chuvashia/palaces", PalacesJSON.serializer().list),
    ChuvashiaLibrariesJson("chuvashia/libraries", LibrariesJSON.serializer().list),
    ChuvashiaTheatresJson("chuvashia/theatres", TheatresJSON.serializer().list),
    ChuvashiaMuseumsJson("chuvashia/museums", MuseumJSON.serializer().list);

    fun getFileRefByName() = DirRef.json file jsonName dot "json"
}
