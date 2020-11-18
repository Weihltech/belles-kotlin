package dev.weihl.belles.data.remote

import android.content.Context
import androidx.annotation.NonNull
import dev.weihl.belles.data.SexyImage
import dev.weihl.belles.data.SexyPage
import dev.weihl.belles.data.local.entity.Belles
import org.jsoup.Jsoup
import timber.log.Timber

/**
 *
 */
class SexyRequest(applicationContext: Context) {

    private var context: Context = applicationContext

    companion object {
        private val WEB_HOST = "https://www.mm131.net"
        private val SEXY = "xinggan"
    }

    init {
        Timber.tag("SexyRequest")
    }

    interface CallBack {
        fun result(bellesList: ArrayList<Belles>)
    }

    private fun getSexyUrl(page: Int): String {
        if (page < 2) {
            return "$WEB_HOST/$SEXY/";
        }
        return "$WEB_HOST/$SEXY/list_6_$page.html";
    }

//    private fun bellesDao(): BellesDao {
//        return AppDatabase.getInstance(context).bellesDao
//    }


    fun loadSexyPageList(page: Int): ArrayList<SexyPage> {
        val url = getSexyUrl(page)
        Timber.d("Load Group Url : $url")

        val bellesList = ArrayList<SexyPage>()
        val document = Jsoup.connect(url).get()
        val elements = document.getElementsByClass("list-left public-box")
        val ddElements = elements[0].getElementsByTag("dd")
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
                val sexyPage = SexyPage(SEXY, href, alt)
                bellesList.add(sexyPage)
                Timber.d(sexyPage.toString())
            } catch (ex: IndexOutOfBoundsException) {
                continue
            }
        }
        return bellesList
    }

    fun loadSexyPageDetailList(@NonNull sexyPage: SexyPage): ArrayList<SexyImage> {
        val pageUrl = sexyPage.href
        Timber.d("Load Page Detail Url : $pageUrl")

        val document = Jsoup.connect(pageUrl).get()
        val elements = document.getElementsByClass("content-page")
        val pageNum = elements[0].getElementsByClass("page-ch")[0]
            .text().replace("共", "").replace("页", "")
        var img = document.getElementsByClass("content-pic")[0]
            .getElementsByTag("img")[0].attr("src")

        val count = Integer.valueOf(pageNum)
        Timber.d("size : $count")
        val bellesImgList = ArrayList<SexyImage>()
        bellesImgList.add(SexyImage(pageUrl, img))

        img = img.substring(0, img.lastIndex - 4)
        for (index in 2..count) {
            bellesImgList.add(
                SexyImage(
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