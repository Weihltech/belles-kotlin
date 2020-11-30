package dev.weihl.belles.data.remote.request

import android.content.Context
import dev.weihl.belles.data.BellesImage
import dev.weihl.belles.data.BellesPage
import dev.weihl.belles.data.remote.BellesRequest
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import timber.log.Timber

/**
 * Des:
 * 古典服装
 *
 * @author Weihl
 * Created 2020/11/20
 */

class ClassicRequest(context: Context) : BellesRequest() {


    companion object {
        private const val WEB_HOST_BASIC = "https://www.tupianzj.com"
        private const val WEB_HOST = "${WEB_HOST_BASIC}/meinv"
        private const val GuZhuang = "guzhuang"
    }

    init {
        Timber.tag("ClassicRequest")
    }

    override fun getPageUrl(page: Int): String {
        if (page < 2) {
            return "$WEB_HOST/$GuZhuang/";
        }
        return "$WEB_HOST/$GuZhuang/list_177_$page.html";
    }


    override fun loadPageList(page: Int): ArrayList<BellesPage> {
        val url = getPageUrl(page)
        Timber.d("Load Group Url : $url")

        val bellesList = ArrayList<BellesPage>()
        val document = Jsoup.connect(url).get()
        val elements = document.getElementsByClass("list_con_box_ul")
        val ddElements = elements[0].getElementsByTag("li")
        for (element in ddElements) {
            val aEls = element.getElementsByTag("a")
            val href = aEls[0].attr("href")
            val imgEls = element.getElementsByTag("img")
            try {
                val imgElsFirst = imgEls[0]
                val alt = imgElsFirst.attr("alt")
//                val src = imgElsFirst.attr("src")
//                val width = imgElsFirst.attr("width")
//                val height = imgElsFirst.attr("height")
                val bellesPage = BellesPage(GuZhuang, "${WEB_HOST_BASIC}${href}", alt)
                bellesList.add(bellesPage)
                Timber.d(bellesPage.toString())
            } catch (ex: IndexOutOfBoundsException) {
                continue
            }
        }
        return bellesList
    }

    override fun loadPageImageList(bellesPage: BellesPage): ArrayList<BellesImage> {
        val pageUrl = bellesPage.href
        Timber.d("Load Page Detail Url : $pageUrl")

        val document = Jsoup.connect(pageUrl).get()
        val elements = document.getElementsByClass("pages")
        val pageStr = elements[0].text().toString()
        val pageCountStr = pageStr.substring(0, pageStr.indexOf("页"))
            .replace("共", "")
        val pageCount = pageCountStr.toString().toInt()
        Timber.d("Data : $pageCountStr ; $pageCount")

        // sub page list
        val pageList = ArrayList<BellesPage>()
        // other all sub page , pageCount
        for (index in 2..pageCount) {
            val subPageUrl = bellesPage.href.replace(".html", "_$index.html")
            val tempPage = BellesPage(GuZhuang, subPageUrl, bellesPage.title)
            pageList.add(tempPage)
        }

        val bellesImageList = ArrayList<BellesImage>()
        // current big pic
        val currentBigPic = findBigPic(document)
        val currentBellesImage = BellesImage(bellesPage.href, currentBigPic)
        bellesImageList.add(currentBellesImage)
        Timber.d(currentBellesImage.toString())
        for (subPage in pageList) {
            val subDocument = Jsoup.connect(subPage.href).get()
            val subBigPic = findBigPic(subDocument)
            val bellesImage = BellesImage(subPage.href, subBigPic)
            Timber.d(bellesImage.toString())
            bellesImageList.add(bellesImage)
        }

        return bellesImageList
    }

    private fun findBigPic(document: Document): String {
        val bigPicElement = document.getElementById("bigpicimg")
        return bigPicElement.attr("src")
    }

    private fun printBellesPage(pageList: ArrayList<BellesPage>) {
        for (page in pageList) {
            Timber.d(page.toString())
        }
    }


}