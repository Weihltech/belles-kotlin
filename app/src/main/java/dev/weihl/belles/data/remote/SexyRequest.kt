package dev.weihl.belles.data.remote

import android.content.Context
import androidx.annotation.NonNull
import com.google.gson.Gson
import dev.weihl.belles.data.local.AppDatabase
import dev.weihl.belles.data.local.dao.BellesDao
import dev.weihl.belles.data.local.entity.Belles
import dev.weihl.belles.work.bean.WorkBelles
import dev.weihl.belles.work.bean.WorkBellesImg
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
        Timber.tag("CrawlerSexyWork")
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

    private fun bellesDao(): BellesDao {
        return AppDatabase.getInstance(context).bellesDao
    }

    fun load(page: Int, @NonNull callBack: CallBack) {

        val bellesList = ArrayList<Belles>()
        val workBellesList = loadGroupPageList(page)
        for (workBelles in workBellesList) {

            val belles = bellesDao().queryBellesByHref(workBelles.href)
            if (belles != null) {
                bellesList.add(belles)
                continue
            }

            val imgList = loadPageDetailList(workBelles.href)
            workBelles.details = Gson().toJson(imgList)
            Timber.d(workBelles.toString())
            insertWorkBelles(workBelles)

            bellesList.add(
                Belles(
                    0,
                    workBelles.title,
                    workBelles.href,
                    workBelles.thumb,
                    workBelles.thumbWh,
                    workBelles.tab,
                    "no",
                    workBelles.details,
                    System.currentTimeMillis(),
                    workBelles.referer
                )
            )

        }

        callBack.result(bellesList)
    }

    fun loadGroupPageList(page: Int): ArrayList<WorkBelles> {
        val url = getSexyUrl(page)
        Timber.d("Load Group Url : $url")


        val bellesList = ArrayList<WorkBelles>()
        val document = Jsoup.connect(url).get()
        val elements = document.getElementsByClass("list-left public-box")
        val ddElements = elements[0].getElementsByTag("dd")
        for (element in ddElements) {
            val aEls = element.getElementsByTag("a")
            val href = aEls[0].attr("href")
            val imgEls = element.getElementsByTag("img")
            try {
                val imgElsFirst = imgEls[0]
                val src = imgElsFirst.attr("src")
                val alt = imgElsFirst.attr("alt")
                val width = imgElsFirst.attr("width")
                val height = imgElsFirst.attr("height")
                val workBelles = WorkBelles(
                    href, alt, src, "$height@$width", "xinggan", "", url
                )
                bellesList.add(workBelles)
                Timber.d(workBelles.toString())
            } catch (ex: IndexOutOfBoundsException) {
                continue
            }

            break
        }
        return bellesList
    }

    fun loadPageDetailList(pageUrl: String): ArrayList<WorkBellesImg> {
        Timber.d("Load Page Detail Url : $pageUrl")

        val document = Jsoup.connect(pageUrl).get()
        val elements = document.getElementsByClass("content-page")
        val pageNum = elements[0].getElementsByClass("page-ch")[0]
            .text().replace("共", "").replace("页", "")
        var img = document.getElementsByClass("content-pic")[0]
            .getElementsByTag("img")[0].attr("src")

        val count = Integer.valueOf(pageNum)
        Timber.d("size : $count")
        val bellesImgList = ArrayList<WorkBellesImg>()
        bellesImgList.add(WorkBellesImg(pageUrl, img))

        img = img.substring(0, img.lastIndex - 4)
        for (index in 2..count) {
            val workBellesImg = WorkBellesImg(
                pageUrl.replace(".html", "_${index}.html"),
                "${img}${index}.jpg"
            )
            bellesImgList.add(workBellesImg)
        }

        for (workBellesImg in bellesImgList) {
            Timber.d("${workBellesImg.referer} ; ${workBellesImg.url}")
        }

        return bellesImgList
    }

    private fun insertWorkBelles(it: WorkBelles) {
        val bellesDao = AppDatabase.getInstance(context).bellesDao
        bellesDao.insert(
            Belles(
                0, it.title, it.href, it.thumb, it.thumbWh, it.tab, "no",
                it.details, System.currentTimeMillis(), it.referer
            )
        )
    }
}