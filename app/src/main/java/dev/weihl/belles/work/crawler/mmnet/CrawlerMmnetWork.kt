package dev.weihl.belles.work.crawler.mmnet

import android.content.Context
import com.google.gson.Gson
import dev.weihl.belles.currDateYyyyMmDd
import dev.weihl.belles.data.local.AppDatabase
import dev.weihl.belles.data.local.entity.Belles
import dev.weihl.belles.work.bean.WorkBelles
import dev.weihl.belles.work.bean.WorkExtraImg
import dev.weihl.belles.work.crawler.allowCrawlerMmnetWork
import dev.weihl.belles.work.crawler.setCrawlerMmnetWorkTime
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

            setCrawlerMmnetWorkTime(context, currDateYyyyMmDd())
        }
    }

    private fun xingganTab() {
        println("xinggan !")
        val tab = "xinggan"

        val pageUrl = "$WEB_HOST$tab/"
        targetTabPage(tab, pageUrl)
    }

    private fun insertWorkBelles(it: WorkBelles) {
        val bellesDao = AppDatabase.getInstance(context).bellesDao
        bellesDao.insert(
            Belles(
                0, it.title, it.href, it.thumb, it.thumbWh, it.tab, 0,
                it.details, System.currentTimeMillis(), it.referer
            )
        )
    }

    private fun targetTabPage(tab: String, pageUrl: String): ArrayList<WorkBelles> {
        println("targetTabPage !")

        val bellesList = ArrayList<WorkBelles>()
        try {

            for (page in 0..1) {
                var url = pageUrl
                if (page >= 1) {
                    url = pageUrl + "list_3_" + page + ".html"
                }
                if (containInfo(tab, pageUrl)) {
                    println("has crawler ; pageUrl = $pageUrl")
                    continue
                }
                val document = Jsoup.connect(url).get()
                val elements = document.getElementsByClass("list-left public-box")
                val ddElements = elements[0].getElementsByTag("dd")
                for (element in ddElements) {
                    val aEls = element.getElementsByTag("a")
                    val href = aEls[0].attr("href")
                    val imgEls = element.getElementsByTag("img")
                    val imgElsFirst = imgEls[0]
                    val src = imgElsFirst.attr("src")
                    val alt = imgElsFirst.attr("alt")
                    val width = imgElsFirst.attr("width")
                    val height = imgElsFirst.attr("height")
                    val workBelles = WorkBelles(
                        href, alt, src, "$height@$width", "xinggan", "", url
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
                    workBelles.details = targetTabPageDetails(workBelles)
                    insertWorkBelles(workBelles)
                }
                recordInfo(tab, pageUrl)
            }
        } catch (e: Exception) {
            println("targetTabPage !" + e.message)
        }

        return bellesList
    }


    private fun targetTabPageDetails(workBelles: WorkBelles): String {

        try {
            val itemPageUrl = workBelles.href;
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

            // 用大图第一个替换为高清缩略视图展示；
            workBelles.thumb = array[0].url
            workBelles.referer = array[0].referer
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