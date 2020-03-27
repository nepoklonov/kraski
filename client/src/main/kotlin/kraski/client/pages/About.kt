package kraski.client.pages

import kotlinx.css.margin
import kotlinx.css.marginBottom
import kotlinx.css.padding
import kotlinx.css.px
import kotlinx.html.ATarget
import kraski.client.highlightedSpan
import kraski.client.indentedDiv
import kraski.client.redH3
import kraski.client.stucture.PageProps
import kraski.client.stucture.StaticPage
import kraski.common.General
import kraski.common.GeneralInfo
import kraski.common.interpretation.Documents
import kraski.common.interpretation.Pages
import kraski.common.models.participants.FormType
import react.dom.a
import react.dom.p
import react.router.dom.navLink
import styled.StyledDOMBuilder
import styled.css
import styled.styledA
import styled.styledDiv

class AboutComponent(props: PageProps) : StaticPage(props) {
    override fun StyledDOMBuilder<*>.page() {

        styledDiv {
            css {
                padding(0.px, 30.px)
                child("p") {
                    margin(10.px, 0.px)
                }
            }
            styledA(href = Documents.conceptionPDF.path) {
                css {
                    marginBottom = 20.px
                }
                +"Скачать «Концепцию этнофестиваля \"Краски Чувашии — 2020\".pdf»"
            }
            redH3 {
                +"О фестивале"
            }
            p {
                +"Фестиваль проводится ежегодно, начиная с 2016 г., в целях сохранения и популяризации самобытной материальной и духовной культуры Чувашии посредством творческого восприятия и глубокого осмысления культурно-исторического наследия чувашского народа среди детей и молодежи."
            }
            redH3 {
                +"Задачи фестиваля"
            }
            indentedDiv { +"Привлечение внимания участников Конкурса к изучению и осмыслению народной культуры, материального и духовного наследия чувашского народа;" }
            indentedDiv { +"Развитие творческих способностей, фантазии по средствам осмысления и творческого восприятия исторического и этнокультурного материала;" }
            indentedDiv { +"Развитие творческих способностей, фантазии по средствам осмысления и творческого восприятия исторического и этнокультурного материала;" }
            indentedDiv { +"Объединение представителей чувашских землячеств России и зарубежных стран на основе участия в организации Конкурса;" }
            indentedDiv { +"Популяризация лучших педагогических и методических практик работы с этно-культурным материалом;" }
            indentedDiv { +"Популяризация чувашской народной культуры, памятников национальной литературы и искусства." }
            redH3 { +"Участники" }
            indentedDiv {
                highlightedSpan { +"дети" }
                +" (обучающиеся в дошкольных отделениях образовательных комплексов, учащиеся в общеобразовательных и специализированных школах, учреждениях дополнительного образования детей, читателей детских библиотек);"
            }
            indentedDiv {
                highlightedSpan { +"молодёжь" }
                +" (воспитанники подростково-молодежных центров и клубов, члены молодежных общественных организаций, индивидуальные участники);"
            }
            indentedDiv {
                highlightedSpan { +"специалисты" }
                +" государственных и частных учреждений образования и культуры (учителя, преподаватели, воспитатели, педагоги-организаторы, методисты, библиотекари)."
            }

            redH3 { +"Конкурсы" }
            listOf(FormType.Art, FormType.Photos, FormType.Music, FormType.Dance, FormType.Professional, FormType.Scientific, FormType.Literature).forEach {
                indentedDiv { +it.title }
            }

            redH3 { +"Сроки" }
            indentedDiv {
                highlightedSpan { +"первый этап" }
                +" (17.02.2020 г. — 01.05.2020 г.) — загрузка конкурсных работ на официальный сайт Фестиваля;"
            }
            indentedDiv {
                highlightedSpan { +"второй этап" }
                +" (05.04.2020 г. — 30.05.2020 г.) — работа Жюри, определение финалистов, информирование о результатах Фестиваля;"
            }
            indentedDiv {
                highlightedSpan { +"третий этап" }
                +" (в преддверии Дня Чувашской Республики) — награждение лауреатов Фестиваля и демонстрация работ финалистов Фестиваля"
            }
            redH3 { +"Принять участие легко" }
            p {
                +"Для участия в Конкурсе необходимо:"
            }
            indentedDiv { +"внимательно прочитать Положение и выбрать номинацию;" }
            indentedDiv {
                +"изучить литературу, иллюстративные и справочные материалы в разделе "
                navLink(Pages.chuvashia.path) { +"«О Чувашии»" }
                +" на сайте Конкурса;"
            }
            indentedDiv { +"подготовить конкурсную работу;" }
            indentedDiv {
                +"загрузить работу, нажав кнопку "
                navLink(Pages.join.path) { +"«Принять участие»" }
                +"и заполнив анкету на сайте Конкурса."
            }
            p { +"При успешной загрузке, участнику придет подтверждение на указанный адрес электронной почты и сертификат участника Конкурса." }

            redH3 { +"Есть вопрос" }
            indentedDiv {
                +"E-mail: "
                +General.eMail
            }
            indentedDiv {
                +"Официальный сайт Конкурса:"
                a(href = "https://kraski-chuvashii.ru/", target = ATarget.blank) {
                    +"https://kraski-chuvashii.ru/"
                }
            }
            indentedDiv {
                +"Официальные страницы Конкурса в социальных сетях:"
                a(href = "https://kraski-chuvashii.ru/", target = ATarget.blank) {
                    +"https://kraski-chuvashii.ru/"
                }
                a(href = "https://vk.com/kraski_chuvashii", target = ATarget.blank) {
                    +"ВКонтакте"
                }
                +","
                a(href = "facebook.com/kraskichuvashii", target = ATarget.blank) {
                    +"Фейсбук"
                }
                +","
                a(href = "instagram.com/kraski_chuvashi", target = ATarget.blank) {
                    +"Инстаграм"
                }
                +"."
            }
            indentedDiv { +"Общая координация Конкурса: Головачев Владимир Сергеевич, e-mail: vladgolovachev@yandex.ru, м.т.: +7 (961) 557-12-55" }
            indentedDiv { +"Республика Чувашия (все районы): Скворцова Лариса Анатольевна, e-mail:zoli@publib.cbx.ru, м.т. +7 (927) 860-71-50;" }
            indentedDiv { +"Чебоксары: Шелтукова Юлия Сергеевна, e-mail: sheltukova93@mail.ru, м.т. +7 (960) 302-82-25;"}
            indentedDiv { +"Москва и Московская область: Суворова Виктория Николаевна e-mail: metodist825@mail.ru, м.т.: +7 (967) 103-70-58;" }
            indentedDiv { +"Санкт-Петербург и Ленинградская область: Рогозная Дарья Александровна, e-mail: d.rogoznaya@mail.ru, м.т.: +7 (921) 972-74-2" }
        }
    }
}