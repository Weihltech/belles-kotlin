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
sealed class Mm131Request(pageUrl: String = "https://www.mm131.net", tab: String = "xinggan") :
    AlbumRequest(pageUrl, tab) {

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
            }.onFailure {
                Timber.d(it)
            }

        }

        return albumList
    }

    override fun analysisAlbumCoverSimilar(albumCover: String): String {
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
}

/**
 * tab 类型请求
 */
open class TabMm131Request(val page: Int, private val defaultTab: String) : Mm131Request() {

    init {
        pageUrl = "https://www.mm131.net/${defaultTab}/list_6_$page.html"
        tab = defaultTab

    }
}

// 性感美眉
class SexyMm131Request(page: Int, defaultTab: String = "xinggan") :
    TabMm131Request(page, defaultTab)

// 清纯美眉
class PureMm131Request(page: Int, defaultTab: String = "qingchun") :
    TabMm131Request(page, defaultTab)

// 校花美眉
class CampusMm131Request(page: Int, defaultTab: String = "xiaohua") :
    TabMm131Request(page, defaultTab)

// 汽车美眉
class CarMm131Request(page: Int, defaultTab: String = "chemo") :
    TabMm131Request(page, defaultTab)

// 旗袍美眉
class QipaoMm131Request(page: Int, defaultTab: String = "qipao") :
    TabMm131Request(page, defaultTab)