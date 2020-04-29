package dev.weihl.belles.work

import android.content.Context
import com.google.gson.Gson
import dev.weihl.belles.data.local.AppDatabase
import dev.weihl.belles.data.local.entity.Belles
import dev.weihl.belles.data.pref.allowCrawlerMmnetWork
import dev.weihl.belles.work.bean.WorkBelles
import dev.weihl.belles.work.bean.WorkExtraImg
import org.jsoup.Jsoup

/**
 * @desc 任务执行
 *
 * @author Weihl Created by 2019/12/5
 *
 */

class CrawlerMmnetWork(applicationContext: Context) {

    private var context: Context = applicationContext

    companion object {
        private var WEB_HOST = "https://www.mm131.net/"
    }

    fun run() {
        if (allowCrawlerMmnetWork(context)) {
            xingganTab()
        }
    }

    private fun xingganTab() {
        println("xinggan !")
        val tab = "xinggan"

        val pageUrl = "$WEB_HOST$tab/"
        targetTabPage(pageUrl)

    }

    private fun installWorkBelles(it: WorkBelles) {
        val bellesDao = AppDatabase.getInstance(context).bellesDao
        bellesDao.insert(
            Belles(
                0,
                it.title,
                it.href,
                it.thumb,
                it.thumbWh,
                it.tab,
                "no",
                it.details,
                System.currentTimeMillis(),
                it.referer
            )
        )
    }

    private fun targetTabPage(pageUrl: String): ArrayList<WorkBelles> {
        println("targetTabPage !")

        var bellesList = ArrayList<WorkBelles>()
        try {

            for (page in 0..1) {
                var url = pageUrl
                if (page >= 1)
                    url = pageUrl + "list_3_" + page + ".html"
                val document = Jsoup.connect(url).get()
                val elements = document.getElementsByClass("list-left public-box")
                val ddElements = elements.get(0).getElementsByTag("dd")
                for (element in ddElements) {
                    val aEls = element.getElementsByTag("a")
                    val href = aEls.get(0).attr("href")
                    val imgEls = element.getElementsByTag("img")
                    val src = imgEls.get(0).attr("src")
                    val alt = imgEls.get(0).attr("alt")
                    val width = imgEls.get(0).attr("width")
                    val height = imgEls.get(0).attr("height")
                    val workBelles = WorkBelles(
                        href,
                        alt,
                        src,
                        "$height@$width",
                        "xinggan",
                        "",
                        url
                    )
                    bellesList.add(workBelles)
                    println(
                        "tab = " + workBelles.tab +
                                " ; title = " + workBelles.title +
                                " ; thumb = " + workBelles.thumb +
                                " ; thumbWh = " + workBelles.thumbWh +
                                " ; href = " + workBelles.href +
                                " ; referer = " + workBelles.referer
                    )
                    workBelles.details = targetTabPageDetails(workBelles.href)
                    installWorkBelles(workBelles)
                }
            }
        } catch (e: Exception) {
            println("targetTabPage !" + e.message)
        }

        return bellesList
    }


    private fun targetTabPageDetails(itemPageUrl: String): String {

        try {
            val referer = itemPageUrl.replace(".html", "")
            val document = Jsoup.connect(itemPageUrl).get()
            val firstImg = document.getElementsByClass("content-pic")[0]
                .getElementsByTag("img")[0].attr("src")
            val elements = document.getElementsByClass("content-page")
            val pageNum = elements[0].getElementsByClass("page-ch")[0]
                .text().replace("共", "").replace("页", "")
            val pageNumInt = Integer.valueOf(pageNum)
            val array = ArrayList<WorkExtraImg>()
            array.add(WorkExtraImg(referer, firstImg))
            for (i in 2..pageNumInt) {
                val pageUrl = referer + "_" + i + ".html"
                val imgUrl = findImgUlr(pageUrl)
                if ("" != imgUrl) {
                    array.add(WorkExtraImg(pageUrl, imgUrl))
                }
            }

            val result = Gson().toJson(array)
            println("Result = $result")
            return result
        } catch (e: Exception) {
            println("Result = ${e.message}")
        }

        return ""
    }

    private fun findImgUlr(url: String): String {
        try {
            val document = Jsoup.connect(url).get()
            return document.getElementsByClass("content-pic")[0]
                .getElementsByTag("img")[0].attr("src")
        } catch (e: Exception) {
            println("Result = ${e.message}")
        }

        return ""
    }

}