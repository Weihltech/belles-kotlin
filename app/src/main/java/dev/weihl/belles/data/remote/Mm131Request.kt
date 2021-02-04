package dev.weihl.belles.data.remote

import dev.weihl.belles.data.BAlbum
import org.jsoup.nodes.Document
import timber.log.Timber


/**
 * 网站 www.mm131.net
 *
 * 实现 AlbumRequest 具体页面爬虫逻辑
 * @author Ngai
 * @since 2021/1/27
 */
sealed class Mm131Request : AlbumPageRequest() {

    // 即 page url 类型切换拼装 标签
    abstract val urlTag: String

    // 根据游标创建 url
    override val pageUrl: String
        get() = if (page < 2) "$HOST_URL/${tab}/" else "$HOST_URL/${tab}/${urlTag}$page.html"

    private var _maxPage: Int = 0
    val maxPage: Int
        get() = _maxPage

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
                val src = imgElsFirst.attr("src")
                //val width = imgElsFirst.attr("width")
                //val height = imgElsFirst.attr("height")
                val iBAlbum = BAlbum(href, alt, tab, src)
                albumList.add(iBAlbum)
                Timber.d(iBAlbum.toString())
            }
        }

        runCatching {
            val pageElements = pageDocument.getElementsByClass("page-en")
            val pageEns = pageElements[pageElements.size - 1].attr("href")
            val maxPage = pageEns.replace(".html", "").split("_").last()
            _maxPage = maxPage.toInt()
            Timber.d("maxPage:$maxPage")
        }

        return albumList
    }

    override fun albumIncreasePageImage(
        albumPageHref: String,
        albumFirstImageUrl: String,
        page: Int
    ): String {
        return "${analysisAlbumCoverSimilar(albumFirstImageUrl)}${page}.jpg"
    }

    override fun albumIncreasePageHref(
        albumFirstDocument: Document,
        albumFirstHref: String,
        page: Int
    ): String {
        return albumFirstHref.replace(".html", "_${page}.html")
    }

    private fun analysisAlbumCoverSimilar(albumCover: String): String {
        return albumCover.substring(0, albumCover.lastIndex - 4)
    }

    override fun analysisAlbumPageImage(albumFirstDocument: Document): String {
        return albumFirstDocument.getElementsByClass("content-pic")[0]
            .getElementsByTag("img")[0].attr("src")
    }

    override fun analysisAlbumPageNum(albumFirstDocument: Document): Int {
        val elements = albumFirstDocument.getElementsByClass("content-page")
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
    override val tab: String = EnumAlbum.SEXY.tab,
    override val urlTag: String = EnumAlbum.SEXY.urlTag
) : Mm131Request()

// 清纯美眉
class PureMm131Request(
    override val tab: String = EnumAlbum.PURE.tab,
    override val urlTag: String = EnumAlbum.PURE.urlTag
) : Mm131Request()

// 校花美眉
class CampusMm131Request(
    override val tab: String = EnumAlbum.CAMPUS.tab,
    override val urlTag: String = EnumAlbum.CAMPUS.urlTag
) : Mm131Request()

// 汽车美眉
class CarMm131Request(
    override val tab: String = EnumAlbum.CAR_MM.tab,
    override val urlTag: String = EnumAlbum.CAR_MM.urlTag
) : Mm131Request()

// 旗袍美眉
class QipaoMm131Request(
    override val tab: String = EnumAlbum.QI_PAO.tab,
    override val urlTag: String = EnumAlbum.QI_PAO.urlTag
) : Mm131Request()
