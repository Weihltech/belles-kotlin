package dev.weihl.belles.data.remote.request.mm131

import dev.weihl.belles.data.BAlbum
import dev.weihl.belles.data.remote.request.AlbumRequest
import org.jsoup.nodes.Document
import timber.log.Timber


/**
 * 网站 www.mm131.net
 *
 * 实现 AlbumRequest 具体页面爬虫逻辑
 * @author Ngai
 * @since 2021/1/27
 */
sealed class Mm131Request(val page: Int) : AlbumRequest() {

    abstract val subTag: String

    override val pageUrl: String
        get() = if (page < 2) "${HOST_URL}/${tab}/" else "${HOST_URL}/${tab}/${subTag}$page.html"


    override fun analysisPageDocument(pageDocument: Document): List<BAlbum> {
        val albumList = mutableListOf<BAlbum>()

        val elements = pageDocument.getElementsByClass("list-left public-box")
        val ddElements = elements[0].getElementsByTag("dd")
        for (element in ddElements) {
            val aEls = element.getElementsByTag("a")
            val href = aEls[0].attr("href")
            val imgEls = element.getElementsByTag("img")

            runCatching {
                val imgElsFirst = imgEls[0]
                val alt = imgElsFirst.attr("alt")
                //val src = imgElsFirst.attr("src")
                //val width = imgElsFirst.attr("width")
                //val height = imgElsFirst.attr("height")
                val iBAlbum = BAlbum(href, alt, tab)
                albumList.add(iBAlbum)
                Timber.d(iBAlbum.toString())
            }
        }

        return albumList
    }

    override fun albumPageImage(albumPageHref: String, albumCover: String, index: Int): String {
        return "${analysisAlbumCoverSimilar(albumCover)}${index}.jpg"
    }

    override fun albumPageHref(albumDocument: Document, href: String, page: Int): String {
        return href.replace(".html", "_${page}.html")
    }

    private fun analysisAlbumCoverSimilar(albumCover: String): String {
        return albumCover.substring(0, albumCover.lastIndex - 4)
    }

    override fun analysisAlbumCover(albumDocument: Document): String {
        return albumDocument.getElementsByClass("content-pic")[0]
            .getElementsByTag("img")[0].attr("src")
    }

    override fun analysisAlbumPageNum(albumDocument: Document): Int {
        val elements = albumDocument.getElementsByClass("content-page")
        val pageNum = elements[0].getElementsByClass("page-ch")[0]
            .text().replace("共", "").replace("页", "")
        runCatching {
            return Integer.valueOf(pageNum)
        }
        return 0
    }

    companion object {
        private const val HOST_URL: String = "https://www.mm131.net"
    }
}

// 性感美眉
class SexyMm131Request(
    page: Int,
    override val tab: String = "xinggan",
    override val subTag: String = "list_6_"
) : Mm131Request(page)

// 清纯美眉
class PureMm131Request(
    page: Int,
    override val tab: String = "qingchun",
    override val subTag: String = "list_1_"
) : Mm131Request(page)

// 校花美眉
class CampusMm131Request(
    page: Int,
    override val tab: String = "qingchun",
    override val subTag: String = "list_2_"
) : Mm131Request(page)

// 汽车美眉
class CarMm131Request(
    page: Int,
    override val tab: String = "qingchun",
    override val subTag: String = "list_3_"
) : Mm131Request(page)

// 旗袍美眉
class QipaoMm131Request(
    page: Int,
    override val tab: String = "qingchun",
    override val subTag: String = "list_4_"
) : Mm131Request(page)
