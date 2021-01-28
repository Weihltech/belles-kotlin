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
        get() = if (page < 2) "$HOST_URL/${tab}/" else "$HOST_URL/${tab}/${urlTag}$page.html"

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

    override fun albumPageImage(albumPageHref: String, albumCover: String, index: Int): String {
        return analysisAlbumCover(pageDocument(albumPageHref))
    }

    override fun albumPageHref(albumDocument: Document, href: String, page: Int): String {
        return href.replace(".html", "_$page.html")
    }

    override fun analysisAlbumCover(albumDocument: Document): String {
        val bigPicElement = albumDocument.getElementById("bigpicimg")
        return bigPicElement.attr("src")
    }

    override fun analysisAlbumPageNum(albumDocument: Document): Int {
        val elements = albumDocument.getElementsByClass("pages")
        val pageStr = elements[0].text().toString()
        val pageCountStr = pageStr.substring(0, pageStr.indexOf("页"))
            .replace("共", "")
        runCatching {
            return pageCountStr.toInt()
        }
        return 0
    }

    companion object {
        private const val HOST_URL: String = "https://www.tupianzj.com/meinv"
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


