package dev.weihl.belles.work.crawler.mmnet

import dev.weihl.belles.MainApp
import dev.weihl.belles.data.local.AppDatabase
import dev.weihl.belles.data.local.entity.Crawler

/**
 * @desc 关于 爬虫 信息辅助操作类
 *
 * @author Weihl Created by 2020/4/30
 *
 */

val crawlerDao = AppDatabase.getInstance(MainApp.getAppContext()).crawlerDao
private val crawlerMmnetList: ArrayList<String> = ArrayList()
private const val TAG = "MmnetTag"

fun recordInfo(tab: String, page: String) {
    val content = newInfo(tab, page)
    val crawler = Crawler()
    crawler.tag = TAG
    crawler.content = content
    crawlerDao.insert(crawler)
}

private fun newInfo(tab: String, page: String): String {
    return "TAG:$tab;Page:$page"
}

fun containInfo(tab: String, page: String): Boolean {
    return crawlerMmnetList.contains(newInfo(tab, page))
}
