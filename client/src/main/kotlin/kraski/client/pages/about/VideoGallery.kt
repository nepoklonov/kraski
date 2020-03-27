package kraski.client.pages.about

import kraski.client.stucture.PageProps
import kraski.client.stucture.PageState
import kraski.client.stucture.StandardPageComponent
import styled.StyledDOMBuilder
import styled.styledIframe

class VideoGallery(pageProps: PageProps) : StandardPageComponent<PageState>(pageProps) {
    override fun StyledDOMBuilder<*>.page() {
        listOf(
            "//vk.com/video_ext.php?oid=-49700017&id=456239021&hash=e03290c3b6f77fed&hd=2",
            "https://www.youtube.com/embed/6SWwEMbaLYE",
            "https://www.youtube.com/embed/gUQzghGK0-4",
            "https://www.youtube.com/embed/lvQQ-klmPls",
            "//vk.com/video_ext.php?oid=-49700017&id=456239020&hash=0d36c7b30bacdaf9&hd=2",
            "//vk.com/video_ext.php?oid=-49700017&id=456239018&hash=b3ac0417867ce341&hd=2"
        ).forEach {
            styledIframe {
                attrs {
                    src = it
                    width = "853"
                    height = "480"
                    set("frameBorder", "0")
                    set("allowFullScreen", true)
                }
            }
        }
    }
}