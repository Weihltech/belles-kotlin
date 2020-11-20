package dev.weihl.belles.data.remote.request

import android.content.Context
import dev.weihl.belles.data.BellesImage
import dev.weihl.belles.data.BellesPage
import dev.weihl.belles.data.remote.BellesRequest
import org.jsoup.Jsoup
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
        private val WEB_HOST_BASIC = "https://www.tupianzj.com"
        private val WEB_HOST = "${WEB_HOST_BASIC}/meinv"
        private val GuZhuang = "guzhuang"
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
        val pageNum = elements[0].getElementsByClass("page-ch")[0]
            .text().replace("共", "").replace("页", "")
        var img = document.getElementsByClass("content-pic")[0]
            .getElementsByTag("img")[0].attr("src")

        val count = Integer.valueOf(pageNum)
        Timber.d("size : $count")
        val bellesImgList = ArrayList<BellesImage>()
        bellesImgList.add(BellesImage(pageUrl, img))

        img = img.substring(0, img.lastIndex - 4)
        for (index in 2..count) {
            bellesImgList.add(
                BellesImage(
                    pageUrl.replace(".html", "_${index}.html"),
                    "${img}${index}.jpg"
                )
            )
        }

        for (workBellesImg in bellesImgList) {
            Timber.d("${workBellesImg.referer} ; ${workBellesImg.url}")
        }

        return bellesImgList
    }


}