package dev.weihl.belles.data.remote.req

import dev.weihl.belles.data.BAlbum
import org.jsoup.nodes.Document
import timber.log.Timber


/**
 * https://www.tupianzj.com/meinv
 *
 * www.tupianzj.com
 * @author Ngai
 * @since 2021/1/27
 */
sealed class TupianzjRequest : AlbumPageRequest() {

    // page url tag； switch page index tab to tag
    abstract val urlTag: String

    override val pageUrl: String
        get() = if (page < 2) "$HOST_URL/meinv/${tab}/" else "$HOST_URL/meinv/${tab}/${urlTag}$page.html"

    override fun analysisPageDocument(pageDocument: Document): List<BAlbum> {
        val albumList = mutableListOf<BAlbum>()

        val elements = pageDocument.getElementsByClass("list_con_box_ul")
        val ddElements = elements[0].getElementsByTag("li")
        for (element in ddElements) {
            val aEls = element.getElementsByTag("a")
            val href = aEls[0].attr("href")
            val imgEls = element.getElementsByTag("img")
            runCatching {
                val imgElsFirst = imgEls[0]
                val alt = imgElsFirst.attr("alt")
                val src = imgElsFirst.attr("src")
                //val width = imgElsFirst.attr("width")
                //val height = imgElsFirst.attr("height")
                val iBAlbum = BAlbum("$HOST_URL$href", alt, tab, src)
                albumList.add(iBAlbum)
                Timber.d(iBAlbum.toString())
            }
        }

        return albumList
    }

    override fun albumIncreasePageImage(
        albumPageHref: String,
        albumFirstImageUrl: String,
        page: Int
    ): String {
        var cover = ""
        runCatching {
            cover = analysisAlbumPageImage(pageDocument(albumPageHref))
        }
        Timber.d("albumPageImage:${albumPageHref} ; cover：${cover}")
        return cover
    }

    override fun albumIncreasePageHref(
        albumFirstDocument: Document,
        albumFirstHref: String,
        page: Int
    ): String {
        return albumFirstHref.replace(".html", "_$page.html")
    }

    override fun analysisAlbumPageImage(albumFirstDocument: Document): String {
        val bigPicElement = albumFirstDocument.getElementById("bigpicimg")
        return bigPicElement.attr("src")
    }

    override fun analysisAlbumPageNum(albumFirstDocument: Document): Int {
        runCatching {
            val elements = albumFirstDocument.getElementsByClass("pages")
            val pageStr = elements[0].text().toString()
            val pageCountStr = pageStr.substring(0, pageStr.indexOf("页"))
                .replace("共", "")
            Timber.d("PageNum:$pageCountStr")
            return pageCountStr.toInt()
        }
        return 0
    }

    companion object {
        private const val HOST_URL: String = "https://www.tupianzj.com"
    }
}

// 古装美眉
class ClassicTupianzjRequest(
    override val tab: String = EnumAlbum.CLASSIC.tab,
    override val urlTag: String = EnumAlbum.CLASSIC.urlTag
) : TupianzjRequest()

// 艺术美眉
class ArtTupianzjRequest(
    override val tab: String = EnumAlbum.ART.tab,
    override val urlTag: String = EnumAlbum.ART.urlTag
) : TupianzjRequest()

// 性感美眉
//class SexyTupianzjRequest(
//    page: Int,
//    override val tab: String = "xinggan",
//    override val subTag: String = "list_176_"
//) : TupianzjRequest(page)


