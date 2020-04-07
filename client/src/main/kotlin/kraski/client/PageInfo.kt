package kraski.client

import kraski.client.pages.*
import kraski.client.pages.chuvashia.*
import kraski.client.stucture.PageProps
import kraski.common.interpretation.*
import kraski.client.pages.NewsComponent
import kraski.client.pages.RaskraskaComponent
import kraski.client.pages.StoriesComponent
import react.RComponent
import react.RState
import kotlin.reflect.KClass

typealias PageRef = DirRef
typealias PageClass = KClass<out RComponent<PageProps, out RState>>


data class PageInfo(val ref: PageRef, val title: String, val pageClass: PageClass) {
    val url
        get() = ref.path
}

class Section(val self: PageInfo, val pages: List<PageInfo>) {
    val url
        get() = self.url
    val title
        get() = self.title

    constructor(ref: PageRef, title: String, pageClass: PageClass, vararg pagesArray: PageInfo) :
        this(PageInfo(ref, title, pageClass), pagesArray.asList())

    companion object {
        val Main = Section(Pages.main, "Главная", MainComponent::class)
        val Join = Section(Pages.join, "Принять участие", JoinComponent::class,
            PageInfo(Pages.Join.art, "Конкурс художественного творчества имени А. А. Кокеля", JoinComponent::class),
            PageInfo(Pages.Join.photos, "Конкурс фотомастерства имени Г. А. и А. А. Костиных", JoinComponent::class),
            PageInfo(Pages.Join.music, "Конкурс вокально-инструментального творчества имени М. Д. Михайлова", JoinComponent::class),
            PageInfo(Pages.Join.dance, "Конкурс танцевального творчества имени Н. В. Павловой", JoinComponent::class),
            PageInfo(Pages.Join.professional, "Конкурс профессионального мастерства работников учреждений культуры и образования имени Г. Н. Волкова", JoinComponent::class),
            PageInfo(Pages.Join.scientific, "Конкурс исследований в области краеведения и генеалогии имени Н. В. Никольского", JoinComponent::class),
            PageInfo(Pages.Join.literature, "Литературный конкурс имени К. В. Иванова", JoinComponent::class)
        )
        val About = Section(Pages.about, "О фестивале", AboutComponent::class)
        val Chuvashia = Section(Pages.chuvashia, "О Чувашии", ChuvashiaComponent::class,
                PageInfo(Pages.Chuvashia.literature, "Литература о Чувашии", Literature::class),
                PageInfo(Pages.Chuvashia.archives, "Архивы Чувашии", Archives::class),
                PageInfo(Pages.Chuvashia.symbols, "Государственные символы Чувашии", Symbols::class),
                PageInfo(Pages.Chuvashia.palaces, "Дворцы культуры Чувашии", Palaces::class),
                PageInfo(Pages.Chuvashia.libraries, "Библиотеки Чувашии", Libraries::class),
                PageInfo(Pages.Chuvashia.theatres, "Театры Чувашии", Theatres::class),
                PageInfo(Pages.Chuvashia.museums, "Музеи Чувашии", Museums::class)
                )
        val News = Section(Pages.news, "Наши новости", NewsComponent::class)
        val Raskraska = Section(Pages.raskraska, "Раскраски", RaskraskaComponent::class)
        val Stories = Section(Pages.stories, "Сказки", StoriesComponent::class)

        val Gallery = Section(Pages.gallery, "Работы участников", GalleryComponent::class,
                PageInfo(Pages.Gallery.art, "Конкурс художественного творчества имени А. А. Кокеля", GalleryComponent::class),
                PageInfo(Pages.Gallery.photos, "Конкурс фотомастерства имени Г. А. и А. А. Костиных", GalleryComponent::class),
                PageInfo(Pages.Gallery.music, "Конкурс вокально-инструментального творчества имени М. Д. Михайлова", GalleryComponent::class),
                PageInfo(Pages.Gallery.dance, "Конкурс танцевального творчества имени Н. В. Павловой", GalleryComponent::class),
                PageInfo(Pages.Gallery.professional, "Конкурс профессионального мастерства работников учреждений культуры и образования имени Г. Н. Волкова", GalleryComponent::class),
                PageInfo(Pages.Gallery.scientific, "Конкурс исследований в области краеведения и генеалогии имени Н. В. Никольского", GalleryComponent::class),
                PageInfo(Pages.Gallery.literature, "Литературный конкурс имени К. В. Иванова", GalleryComponent::class)
        )
        val Team = Section(Pages.team, "Команда проекта", TeamComponent::class)
        val Partners = Section(Pages.partners, "Партнёры", PartnersComponent::class)
        val History = Section(Pages.history, "История Чувашии", HistoryComponent::class)
        val Contacts = Section(Pages.contacts, "Контакты", ContactsComponent::class)


            val Admin = Section(Pages.admin, "Админка", AdminComponent::class)
    }
}