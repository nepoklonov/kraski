package kraski.client.stucture.header

import kotlinext.js.assign
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.css.Display
import kotlinx.css.properties.TextDecoration
import kotlinx.css.properties.borderBottom
import kotlinx.html.id
import kotlinx.serialization.ContextSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.list
import kraski.client.*
import kraski.client.stucture.GalleryPreview
import kraski.common.*
import kraski.common.interpretation.ImageDirs
import kraski.common.interpretation.JsonRef
import react.*
import react.dom.*
import react.router.dom.navLink
import styled.*
import kotlin.browser.window
import kotlin.js.Date
import kotlin.reflect.KMutableProperty1

val DAY_OF_THE_END = Date.UTC(2020, 6, 1).toLong()
val DAY_OF_THE_STOP = Date.UTC(2020, 1, 20).toLong()
const val msInDay = 1000 * 3600 * 24
var flag = true

fun getDaysLeft() = (DAY_OF_THE_END / msInDay - Date.now().toLong() / msInDay).toInt()
//fun getAccessDaysLeft() = (DAY_OF_THE_STOP / msInDay - Date.now().toLong() / msInDay).toInt()

interface HeaderState : RState {
    var generalInfo: GeneralInfo
    var contacts: List<Contact>
    var partners: List<Partner>
}

inline fun <T, S : RState> RComponent<*, S>.initState(prop: KMutableProperty1<S, T>, default: T, crossinline valueFunction: suspend () -> T) {
    prop.set(state, default)
    GlobalScope.launch {
        val newT = valueFunction()
        setState {
            prop.set(this, newT)
        }
    }
}

inline fun <reified T : Any, S : RState, reified R : Request> RComponent<*, S>.serverState(prop: KMutableProperty1<S, T>, default: T, request: R, serializer: KSerializer<T> = T::class.getSerializer()) {
    prop.set(state, default)
    GlobalScope.launch {
        val newT = request.send().parseAnswerBody(serializer)
        setState {
            prop.set(this, newT)
        }
    }
}

class HeaderComponent : RComponent<RoutedProps, HeaderState>() {
    init {
        serverState(HeaderState::generalInfo, GeneralInfo(), Request.GetGeneralInfo())
        serverState(HeaderState::contacts, listOf(), Request.GetJson(JsonRef.ContactsJson), Contact.serializer().list)
        serverState(HeaderState::partners, listOf(), Request.GetJson(JsonRef.LogosJson), Partner.serializer().list)
    }

    override fun RBuilder.render() {
        infoBlock(state.generalInfo.participantsAmount, getDaysLeft(), state.contacts)
        styledDiv {
            css {
                backgroundImage = Image("url(/images/design/top-back.jpg)")
                height = 140.px
                backgroundSize = "cover"
                backgroundPosition = "center"
                gridArea = "h-back"
            }
        }
        styledDiv {
            css {
                gridArea = "ornament"
                width = 140.px
                height = 100.pct
                backgroundSize = "50%"
                backgroundImage = Image("url(/images/design/background.png)")
                justifySelf = JustifyContent.center
            }
        }
        styledDiv {
            css {
                gridArea = "header"
                display = Display.flex
                justifyContent = JustifyContent.spaceBetween
                alignItems = Align.center
            }
            styledDiv {
                css {
                    color = redKraski
                    display = Display.flex
                    flexDirection = FlexDirection.column
                }
                styledH1 {
                    +General.ruTitle
                }
                styledDiv {
                    css {
                        width = 100.pct
                        height = 10.px
                        background = "url(/images/design/three-up.png) round"
                        backgroundSize = "14px"
                    }
                }
                styledH2 {
                    +General.shortDescription
                }
            }
            child(GalleryPreview::class) {}
            styledDiv {
                css {
                    width = 70.px
                    height = 100.pct
                    backgroundSize = "100%"
                    backgroundImage = Image("url(/images/design/background.png)")
                }
            }
        }
        styledDiv {
            css {
                gridArea = "head-nav"
                display = Display.flex
                alignItems = Align.center
            }
            styledUl {
                listOf(Section.Gallery, Section.Team, Section.Partners,
                        Section.Contacts).forEach {
                    styledLi {
                        css {
                            display = Display.inline
                            textAlign = TextAlign.center
                            paddingBottom = 3.px
                            marginRight = 25.px
                            borderBottom(3.px, BorderStyle.solid, Color.white)
                            hover {
                                borderBottom(3.px, BorderStyle.solid, redKraskiWithOpacity(0.3))
                            }
                            active {
                                borderBottom(3.px, BorderStyle.solid, redKraski)
                            }
                            child("a") {
                                color = redKraski
                                textDecoration = TextDecoration.none
                                fontWeight = FontWeight.bold
                            }
                        }
                        styledImg(src = "images/design/three-down.png") {
                            css {
                                height = 12.px
                                marginRight = 3.px
                            }
                        }
                        navLink(to = it.url) {
                            +it.title
                        }
                    }
                }
            }
        }
        styledDiv {
            css {
                gridArea = "left-nav"
            }
            styledUl {
                css {
                    child("a") {
                        textDecoration = TextDecoration.none
                    }
                    child("a li") {
                        display = Display.flex
                        justifyContent = JustifyContent.center
                        alignItems = Align.center
                        textAlign = TextAlign.center
                        margin(10.px, 0.px)
                        padding(10.px, 0.px)
                        cursor = Cursor.pointer
                    }
                }
                navLink(to = Section.Main.url) {
                    styledLi {
                        styledImg(src = "/images/design/logo-kraski.png") {
                            css.width = 100.pct
                        }
                    }
                }
                listOf(Section.Join).forEach {
                    navLink(to = it.url) {
                        styledLi {
                            css {
                                backgroundColor = redKraskiWithOpacity(0.8)
                                hover {
                                    backgroundColor = redKraskiWithOpacity(0.9)
                                }
                                active {
                                    backgroundColor = redKraski
                                    padding(11.px, 0.px, 9.px, 0.px)
                                }
                                fontWeight = FontWeight.bold
                                textDecoration = TextDecoration.none
                                color = lightYellow
                            }
                            +it.title
                        }
                    }
                }
                listOf(Section.About, Section.News, Section.Stories, Section.Raskraska, Section.History,
                    Section.Chuvashia).forEach {
                    navLink(to = it.url) {
                        styledLi {
                            css {
                                backgroundColor = lightYellow
                                hover {
                                    backgroundColor = mediumYellow
                                }
                                active {
                                    backgroundColor = hardYellow
                                    padding(11.px, 0.px, 9.px, 0.px)
                                }
                                fontWeight = FontWeight.bold
                                textDecoration = TextDecoration.none
                                color = redKraski
                            }
                            +it.title
                        }
                    }
                }
                state.partners.forEach {
                    a(href = it.link) {
                        styledLi {
                            css {
                                backgroundColor = lightYellow
                                hover {
                                    backgroundColor = mediumYellow
                                }
                                active {
                                    backgroundColor = hardYellow
                                    padding(11.px, 0.px, 9.px, 0.px)
                                }
                            }
                            styledImg(src = (ImageDirs.logos / it.logo).path) {
                                css {
                                    width = 70.pct
                                }
                            }
                        }
                    }
                }
            }
        }

        styledDiv {
            css {
                backgroundColor = lightYellow
            }
            styledDiv {
                css {
                    width = 100.pct
                    height = 10.px
                    background = "url(/images/design/three-up.png) round"
                    backgroundSize = 14.px.toString()
                }
            }

            styledDiv {
                css {
                    height = 350.px
                    padding(20.px, 2.pct)
                    display = Display.flex
                    alignItems = Align.center
                    flexWrap = FlexWrap.wrap
                    justifyContent = JustifyContent.spaceBetween
                }
                styledDiv {
                    attrs.id = "vk_groups"
                    css {
                        height = 280.px
                        padding(5.px, 10.px)
                    }
                    if (flag) {
                        js("VK.Widgets.Group(\"vk_groups\", {mode: 4, width: \"280\", height: \"280\"}, 89878265);")
                        flag = false
                    }
                }
                styledDiv {
                    css {
                        height = 280.px
                        padding(5.px, 10.px)
                    }
                    attrs["dangerouslySetInnerHTML"] = InnerHTML("<iframe src=\"https://www.facebook.com/plugins/page.php?href=https%3A%2F%2Fwww.facebook.com%2Fkraski-chuvashii&tabs=timeline&width=280&height=280&small_header=false&adapt_container_width=true&hide_cover=false&show_facepile=true&appId=291393108402246\" width=\"280\" height=\"280\" style=\"border:none;overflow:hidden\" scrolling=\"no\" frameborder=\"0\" allowTransparency=\"true\" allow=\"encrypted-media\"></iframe>")
                }

                styledDiv {
                    css {
                        height = 280.px
                        padding(5.px, 10.px)
                    }
                    attrs["dangerouslySetInnerHTML"] = InnerHTML("\n" +
                            "<!DOCTYPE html> \n" +
                            "<html lang=\"ru\">\n" +
                            "\t<head>\n" +
                            "\t\t<title>inWidget - free Instagram widget for your site!</title>\n" +
                            "\t\t<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />\n" +
                            "\t\t<meta http-equiv=\"content-language\" content=\"ru\" />\n" +
                            "\t\t<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n" +
                            "\t\t<style type=\"text/css\">\n" +
                            "\t\thtml {\n" +
                            "\twidth: 100%;\n" +
                            "\theight: 100%;\n" +
                            "}\n" +
                            "body {\n" +
                            "\twidth: 100%;\n" +
                            "\theight: 100%;\n" +
                            "\tcolor: #212121;\n" +
                            "\tfont-family: arial;\n" +
//                            "\tfont-size:12px;\n" +
                            "\tpadding:0px;\n" +
                            "\tmargin:0px;\n" +
                            "}\n" +
                            "img {\n" +
                            "\tborder: 0;\n" +
                            "}\n" +
                            ".clear {\n" +
                            "\tclear:both;\n" +
                            "\theight:0;\n" +
                            "\tline-height:0;\n" +
                            "}\n" +
                            ".widget {\n" +
                            "\tborder:1px solid #c3c3c3;\n" +
                            "\tbackground:#f9f9f9;\n" +
                            "\tborder-radius: 5px 5px 5px 5px;\n" +
                            "\t-webkit-border-radius: 5px 5px 5px 5px;\n" +
                            "\t-moz-border-radius: 5px 5px 5px 5px;\n" +
                            "\toverflow:hidden;\n" +
                            "}\n" +
                            "\t.widget a.title:link, .widget a.title:visited  {\n" +
                            "\t\tdisplay:block;\n" +
                            "\t\theight:33px;\n" +
                            "\t\ttext-decoration:none;\n" +
                            "\t\t/* Permalink - use to edit and share this gradient: http://colorzilla.com/gradient-editor/#547fa7+0,46719b+100 */\n" +
                            "\t\tbackground: #46719b; /* Old browsers */\n" +
                            "\t\tbackground: -moz-linear-gradient(top, #547fa7 0%, #46719b 100%); /* FF3.6-15 */\n" +
                            "\t\tbackground: -webkit-linear-gradient(top, #547fa7 0%,#46719b 100%); /* Chrome10-25,Safari5.1-6 */\n" +
                            "\t\tbackground: linear-gradient(to bottom, #547fa7 0%,#46719b 100%); /* W3C, IE10+, FF16+, Chrome26+, Opera12+, Safari7+ */\n" +
                            "\t\tfilter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#547fa7', endColorstr='#46719b',GradientType=0 ); /* IE6-9 */\n" +
                            "\t}\n" +
                            "\t.widget .title .icon {\n" +
                            "\t\tdisplay:block;\n" +
                            "\t\tfloat:left;\n" +
                            "\t\twidth:25px;\n" +
                            "\t\theight:25px;\n" +
                            "\t\tbackground:url('data:img/png;base64,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') no-repeat;\n" +
                            "\t\tbackground-size: 25px 25px;\n" +
                            "\t\tmargin:4px 10px 0 5px;\n" +
                            "\t}\n" +
                            "\t\t.widget .title .text {\n" +
                            "\t\t\tfloat:left;\n" +
                            "\t\t\theight:25px;\n" +
                            "\t\t\toverflow:hidden;\n" +
                            "\t\t\tmargin:5px 0 0 0;\n" +
                            "\t\t\tcolor:#FFF;\n" +
//                            "\t\t\tfont-size:18px;\n" +
                            "\t\t\twhite-space:nowrap;\n" +
                            "\t\t}\n" +
                            "\t\t.widget .profile {\n" +
                            "\t\t\twidth:100%;\n" +
                            "\t\t\theight:80px;\n" +
                            "\t\t\tborder-collapse: collapse;\n" +
                            "\t\t}\n" +
                            "\t\t\t.widget .profile tr td {\n" +
                            "\t\t\t\tpadding:0px;\n" +
                            "\t\t\t\tmargin:0px;\n" +
                            "\t\t\t\ttext-align:center;\n" +
                            "\t\t\t}\n" +
                            "\t\t\t.widget .profile td {\n" +
                            "\t\t\t\tborder:1px solid #c3c3c3;\n" +
                            "\t\t\t}\n" +
                            "\t\t\t.widget .profile .avatar {\n" +
                            "\t\t\t\twidth:1%;\n" +
                            "\t\t\t\tpadding:10px !important;\n" +
                            "\t\t\t\tborder-left:none !important;\n" +
                            "\t\t\t\tline-height:0px;\n" +
                            "\t\t\t}\n" +
                            "\t\t\t\t.widget .profile .avatar img {\n" +
                            "\t\t\t\t\twidth:60px;\n" +
                            "\t\t\t\t}\n" +
                            "\t\t\t.widget .profile .value {\n" +
                            "\t\t\t\twidth:33%;\n" +
                            "\t\t\t\theight:30px;\n" +
//                            "\t\t\t\tfont-size:14px;\n" +
                            "\t\t\t\tfont-weight:bold;\n" +
                            "\t\t\t}\n" +
                            "\t\t\t.widget .profile span {\n" +
                            "\t\t\t\tdisplay:block;\n" +
//                            "\t\t\t\tfont-size:9px;\n" +
                            "\t\t\t\tfont-weight:bold;\n" +
                            "\t\t\t\tcolor:#999999;\n" +
                            "\t\t\t\tmargin:-2px 0 0 0;\n" +
                            "\t\t\t}\n" +
                            "\t.widget a.follow:link, .widget a.follow:visited {\n" +
                            "\t\tdisplay:block;\n" +
                            "\t\tbackground:#ad4141;\n" +
                            "\t\ttext-decoration:none;\n" +
//                            "\t\tfont-size:14px;\n" +
                            "\t\tcolor:#FFF;\n" +
                            "\t\tfont-weight:bold;\n" +
                            "\t\twidth:120px;\n" +
                            "\t\tmargin:0 auto 0 auto;\n" +
                            "\t\tpadding:4px 4px 4px 10px;\n" +
                            "\t\tborder:3px solid #FFF;\n" +
                            "\t\tborder-radius: 5px 5px 5px 5px;\n" +
                            "\t\t-webkit-border-radius: 5px 5px 5px 5px;\n" +
                            "\t\t-moz-border-radius: 5px 5px 5px 5px;\n" +
                            "\t\tbox-shadow: 0 0px 2px rgba(0,0,0,0.5);\n" +
                            "\t\t-moz-box-shadow: 0 0px 2px rgba(0,0,0,0.5);\n" +
                            "\t\t-webkit-box-shadow: 0 0px 2px rgba(0,0,0,0.5);\n" +
                            "\t}\n" +
                            "\t.widget a.follow:hover {\n" +
                            "\t\tbackground:#cf3838;\n" +
                            "\t}\n" +
                            "\t.widget .data {\n" +
                            "\t\ttext-align:left;\n" +
                            "\t\tmargin:10px 0 0 10px;\n" +
                            "\t\tpadding:0 0 5px 0;\n" +
                            "\t}\n" +
                            "\t\t.widget .data a.image:link, .widget .data a.image:visited{\n" +
                            "\t\t\tdisplay:block;\n" +
                            "\t\t\tfloat:left;\n" +
                            "\t\t\tmargin:0 5px 5px 0;\n" +
                            "\t\t\toverflow:hidden;\n" +
                            "\t\t\tborder:2px solid #FFF;\n" +
                            "\t\t\tbox-shadow: 0 1px 1px rgba(0,0,0,0.3);\n" +
                            "\t\t\tling-height:0px;\n" +
                            "\t\t\ttext-decoration:none;\n" +
                            "\t\t}\n" +
                            "\t\t.widget .data .image:hover {\n" +
                            "\t\t\tfilter: alpha(opacity=80);\n" +
                            "\t\t \topacity: 0.8;\n" +
                            "\t\t}\n" +
                            "\t\t\t.widget .data .image span {\n" +
                            "\t\t\t\tdisplay:block;\n" +
                            "\t\t\t\tbackground-repeat:no-repeat;\n" +
                            "\t\t\t\tbackground-size:cover;\n" +
                            "\t\t\t\tbackground-position:center center;\n" +
                            "\t\t\t}\n" +
                            "\t.widget .empty {\n" +
                            "\t\ttext-align:center;\n" +
                            "\t\tmargin:10px 0 10px 0;\n" +
                            "\t}\n" +
                            ".copyright {\n" +
                            "\tmargin:3px 0 3px 0;\n" +
//                            "\tfont-size:10px;\n" +
                            "\ttext-align:center;\n" +
                            "}\n" +
                            "\t.copyright a:link, .copyright a:visited {\n" +
                            "\t\ttext-decoration:none;\n" +
                            "\t\tcolor:#666;\n" +
                            "\t}\n" +
                            "\t.copyright a:hover {\n" +
                            "\t\ttext-decoration:underline;\n" +
                            "\t}\n" +
                            ".cacheError {\n" +
//                            "\tfont-size:10px;\n" +
                            "\tcolor:red;\n" +
                            "\ttext-align:center;\n" +
                            "}\n" +
                            "@media (max-width: 240px) {\n" +
                            "\t.widget .profile {\n" +
                            "\t\tdisplay:none;\n" +
                            "\t}\n" +
                            "}\n" +
                            "@media (max-width: 180px) {\n" +
                            "\t.widget .title .text {\n" +
                            "\t\tdisplay:none;\n" +
                            "\t}\n" +
                            "}\n" +
                            "\t\t</style>"+
                            "\t\t\t\t\t<style type='text/css'>\n" +
                            "\t\t\t\t.widget {\n" +
                            "\t\t\t\t\twidth:258px;\n" +
                            "\t\t\t\t}\n" +
                            "\t\t\t\t.widget .data a.image:link, .widget .data a.image:visited {\n" +
                            "\t\t\t\t\twidth:51px;\n" +
                            "\t\t\t\t\theight:51px;\n" +
                            "\t\t\t\t}\n" +
                            "\t\t\t\t.widget .data .image span {\n" +
                            "\t\t\t\t\twidth:51px;\n" +
                            "\t\t\t\t\theight:51px;\n" +
                            "\t\t\t\t}\n" +
                            "\t\t\t\t.copyright, .cacheError {\n" +
                            "\t\t\t\t\twidth:258px;\n" +
                            "\t\t\t\t}\n" +
                            "\t\t\t</style>\n" +
                            "\t\t\t</head>\n" +
                            "<body>\n" +
                            "<div id=\"widget\" class=\"widget\">\n" +
                            "\t<a href=\"https://instagram.com/kraski_chuvashii\" target=\"_blank\" class=\"title\">\n" +
                            "\t\t<div class=\"icon\">&nbsp;</div>\n" +
                            "\t\t<div class=\"text\">Мы в Instagram:</div>\n" +
                            "\t\t<div class=\"clear\">&nbsp;</div>\n" +
                            "\t</a>\n" +
                            "\t\t\t<table class=\"profile\">\n" +
                            "\t\t\t<tr>\n" +
                            "\t\t\t\t<td rowspan=\"2\" class=\"avatar\">\n" +
                            "\t\t\t\t\t<a href=\"http://instagram.com/kraski_chuvashii\" target=\"_blank\"><img src=\"https://scontent-hel2-1.cdninstagram.com/v/t51.2885-19/s150x150/43052082_1952003601549898_5776792976613179392_n.jpg?_nc_ht=scontent-hel2-1.cdninstagram.com&_nc_ohc=W6rsE7lTjVMAX8yd7K-&oh=6341944e79c5e032428146931d9cb18f&oe=5EB9C76F\"></a>\n" +
                            "\t\t\t\t</td>\n" +
                            "\t\t\t\t<td class=\"value\">\n" +
                            "\t\t\t\t\t27\t\t\t\t\t<span>посты</span>\n" +
                            "\t\t\t\t</td>\n" +
                            "\t\t\t\t<td class=\"value\">\n" +
                            "\t\t\t\t\t124 \t\t\t\t\t<span>подписчики</span>\n" +
                            "\t\t\t\t</td>\n" +
                            "\t\t\t\t<td class=\"value\" style=\"border-right:none !important;\">\n" +
                            "\t\t\t\t\t246 \t\t\t\t\t<span>подписки</span>\n" +
                            "\t\t\t\t</td>\n" +
                            "\t\t\t</tr>\n" +
                            "\t\t\t<tr>\n" +
                            "\t\t\t\t<td colspan=\"3\" style=\"border-right:none !important;\">\n" +
                            "\t\t\t\t\t<a href=\"https://instagram.com/kraski_chuvashii\" class=\"follow\" target=\"_blank\">Посмотреть &#9658;</a>\n" +
                            "\t\t\t\t</td>\n" +
                            "\t\t\t</tr>\n" +
                            "\t\t</table>\n" +
                            "\t\t<div id=\"widgetData\" class=\"data\"><a href=\"https://www.instagram.com/p/BvKYVxOgdu_\" class=\"image\" target=\"_blank\"><span style=\"background-image:url(https://scontent-hel2-1.cdninstagram.com/v/t51.2885-15/e35/c77.0.455.455a/51840536_2022212351235218_643789853281096750_n.jpg?_nc_ht=scontent-hel2-1.cdninstagram.com&_nc_cat=102&_nc_ohc=gPvLgA4uTIkAX8If9sg&oh=92a891baaf98ccd525adea26bafab2fc&oe=5EBB3882);\">&nbsp;</span></a><a href=\"https://www.instagram.com/p/B-guquUDv4f\" class=\"image\" target=\"_blank\"><span style=\"background-image:url(https://scontent-hel2-1.cdninstagram.com/v/t51.2885-15/sh0.08/e35/c211.0.1018.1018a/s640x640/92287759_253196025839348_4827367611719624683_n.jpg?_nc_ht=scontent-hel2-1.cdninstagram.com&_nc_cat=108&_nc_ohc=REZhJTxmew4AX9Vfdti&oh=beee188baf1ea98d45a5b59ab2124bec&oe=5EB9EA40);\">&nbsp;</span></a><a href=\"https://www.instagram.com/p/B-mtnXHDBiv\" class=\"image\" target=\"_blank\"><span style=\"background-image:url(https://scontent-hel2-1.cdninstagram.com/v/t51.2885-15/sh0.08/e35/p640x640/91785982_101046264875561_487998468643678225_n.jpg?_nc_ht=scontent-hel2-1.cdninstagram.com&_nc_cat=105&_nc_ohc=qtndxCKbRiAAX_Omldh&oh=12aba720b13ed9249e70cf1ddce7555c&oe=5E932787);\">&nbsp;</span></a><a href=\"https://www.instagram.com/p/B-meVc4DBxv\" class=\"image\" target=\"_blank\"><span style=\"background-image:url(https://scontent-hel2-1.cdninstagram.com/v/t51.2885-15/sh0.08/e35/p640x640/91623706_158705755606771_8959987633158428636_n.jpg?_nc_ht=scontent-hel2-1.cdninstagram.com&_nc_cat=107&_nc_ohc=sznKbPRVq-QAX8uL9GJ&oh=382695a7cd9dcd1927e2c599a07ebec3&oe=5E936EE3);\">&nbsp;</span></a><a href=\"https://www.instagram.com/p/BvCZD3CgIAJ\" class=\"image\" target=\"_blank\"><span style=\"background-image:url(https://scontent-hel2-1.cdninstagram.com/v/t51.2885-15/e35/c81.0.487.487a/52788800_370688043526089_734975537777680863_n.jpg?_nc_ht=scontent-hel2-1.cdninstagram.com&_nc_cat=109&_nc_ohc=oONmeWCf0sEAX9gTi2Q&oh=73286acc5bc6d4ed5ef95cdb6b6c2641&oe=5EBC2369);\">&nbsp;</span></a><a href=\"https://www.instagram.com/p/B-WmMZ8DvU2\" class=\"image\" target=\"_blank\"><span style=\"background-image:url(https://scontent-hel2-1.cdninstagram.com/v/t51.2885-15/sh0.08/e35/c0.5.1219.1219a/s640x640/91285421_3067445923481765_5490263871638981073_n.jpg?_nc_ht=scontent-hel2-1.cdninstagram.com&_nc_cat=105&_nc_ohc=EG4MLNqt9oEAX8TpnFY&oh=68efc679733a21c2d7f6479134ad569e&oe=5EB957C5);\">&nbsp;</span></a><a href=\"https://www.instagram.com/p/B-Y-3U3D2Q5\" class=\"image\" target=\"_blank\"><span style=\"background-image:url(https://scontent-hel2-1.cdninstagram.com/v/t51.2885-15/sh0.08/e35/c0.5.1219.1219a/s640x640/91398383_729390917595459_7894415899679777530_n.jpg?_nc_ht=scontent-hel2-1.cdninstagram.com&_nc_cat=103&_nc_ohc=B5eLQu9LeqYAX__q_Sm&oh=e69b84da7b777833f1c6621a71b0a030&oe=5EB95C0D);\">&nbsp;</span></a><a href=\"https://www.instagram.com/p/B-Wax4bjcWW\" class=\"image\" target=\"_blank\"><span style=\"background-image:url(https://scontent-hel2-1.cdninstagram.com/v/t51.2885-15/sh0.08/e35/c0.5.1219.1219a/s640x640/91147484_898850040570686_4985024415546285588_n.jpg?_nc_ht=scontent-hel2-1.cdninstagram.com&_nc_cat=102&_nc_ohc=NoXN8lK8bscAX-yLb68&oh=2820fc44d6e3f6870b87d8e7328c7561&oe=5EB9F0AE);\">&nbsp;</span></a><a href=\"https://www.instagram.com/p/B4vJ082l4nX\" class=\"image\" target=\"_blank\"><span style=\"background-image:url(https://scontent-hel2-1.cdninstagram.com/v/t51.2885-15/sh0.08/e35/c210.0.659.659a/s640x640/75310181_149687046386781_4456981012344638228_n.jpg?_nc_ht=scontent-hel2-1.cdninstagram.com&_nc_cat=109&_nc_ohc=VIpipvGfn_8AX84cuur&oh=fa5b10a124026794e6810686aa3f5756&oe=5EBA8C32);\">&nbsp;</span></a><a href=\"https://www.instagram.com/p/BzUqbYCgDQe\" class=\"image\" target=\"_blank\"><span style=\"background-image:url(https://scontent-hel2-1.cdninstagram.com/v/t51.2885-15/sh0.08/e35/c198.0.684.684a/s640x640/64593049_141001266997375_1870873078810782696_n.jpg?_nc_ht=scontent-hel2-1.cdninstagram.com&_nc_cat=107&_nc_ohc=YxTSmjz1FJQAX-kWP6l&oh=c4909d472f6eced206568dcc9f91e86b&oe=5EB95352);\">&nbsp;</span></a><a href=\"https://www.instagram.com/p/B-UohRtjzY4\" class=\"image\" target=\"_blank\"><span style=\"background-image:url(https://scontent-hel2-1.cdninstagram.com/v/t51.2885-15/e35/c125.0.510.510a/91095176_1908583792619867_6895979307868400051_n.jpg?_nc_ht=scontent-hel2-1.cdninstagram.com&_nc_cat=111&_nc_ohc=vzz0eO8PEP0AX_cI2xr&oh=2d81596c7864ac849c2aad7be1ad9775&oe=5EBC3DB1);\">&nbsp;</span></a><a href=\"https://www.instagram.com/p/BvbI4JSj2NZ\" class=\"image\" target=\"_blank\"><span style=\"background-image:url(https://scontent-hel2-1.cdninstagram.com/v/t51.2885-15/sh0.08/e35/c170.0.644.644a/s640x640/54429321_371650573427176_786918224528695354_n.jpg?_nc_ht=scontent-hel2-1.cdninstagram.com&_nc_cat=104&_nc_ohc=nNpUSjmPPX4AX9JV9PX&oh=fb9832afd69ac2928d56c321a2933f4f&oe=5EB8BC86);\">&nbsp;</span></a><div class=\"clear\">&nbsp;</div></div></div>\n" +
                            "</body>\n" +
                            "</html>\n" +
                            "<!-- \n" +
                            "\tinWidget - free Instagram widget for your site!\n" +
                            "\thttps://inwidget.ru\n" +
                            "\t© Alexandr Kazarmshchikov\n" +
                            "-->")
                }
            }
            styledDiv {
                css {
                    width = 100.pct
                    height = 10.px
                    background = "url(/images/design/three-up.png) round"
                    backgroundSize = 14.px.toString()
                }
            }

            css {
                gridArea = "footer"
            }
            styledDiv {
                css {
                    padding(20.px, 5.pct)
                    fontWeight = FontWeight.bold
                }
                +"© ${General.ruTitle.quote()}, 2019 — 2020"
                br {}
                +"Полное или частичное копирование материалов запрещено, при согласованном копировании ссылка на ресурс обязательна."
            }
        }
    }

    override fun componentDidMount() {
        window.addEventListener("resize", { forceUpdate() })
    }

    override fun componentWillUnmount() {
        window.removeEventListener("resize", { forceUpdate() })
    }
}