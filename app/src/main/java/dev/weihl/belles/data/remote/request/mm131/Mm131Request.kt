package dev.weihl.belles.data.remote.request.mm131

import dev.weihl.belles.data.BAlbum
import dev.weihl.belles.data.remote.request.AlbumRequest
import org.jsoup.nodes.Document
import timber.log.Timber


/**
 * 网站 www.mm131.net
 * @author Ngai
 * @since 2021/1/27
 */
open class Mm131Request(pageUrl: String = "https://www.mm131.net", tab: String = "xinggan") :
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