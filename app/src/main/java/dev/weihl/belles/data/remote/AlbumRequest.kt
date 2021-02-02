package dev.weihl.belles.data.remote

import dev.weihl.belles.data.BAlbum
import dev.weihl.belles.data.BImage
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import timber.log.Timber


/**
 * 页面数据请求与提取
 * @author Ngai
 * @since 2021/1/27
 */
sealed class AlbumRequest {

    abstract val pageUrl: String

    abstract val tab: String

    protected fun pageDocument(pageUrl: String): Document {
        Timber.d(pageUrl)
        return Jsoup.connect(pageUrl).get()
    }

    fun loadAlbumList(): List<BAlbum> {
        runCatching { return analysisPageDocument(pageDocument(pageUrl)) }
        return mutableListOf()
    }

    // 需要用到 jsoup 爬虫相关知识
    protected abstract fun analysisPageDocument(pageDocument: Document): List<BAlbum>

    // 通过专辑连接，分析并提取专辑图片数据，并填充到专辑对象中
    fun syncAlbumDetails(bAlbum: BAlbum) {
        runCatching {
            // 通过发现在对应规律，采用拼装方式，将图片集合起来
            // 获取专辑第一页信息，数量，但此页面已经访问，先收集
            val albumDocument = pageDocument(bAlbum.href)
            val pageNum = analysisAlbumPageNum(albumDocument)
            val albumImageUrl = analysisAlbumPageImage(albumDocument)
            bAlbum.list.add(BImage(bAlbum.href, albumImageUrl))
            // 第二页面，才需要循环获取
            for (index in 2..pageNum) {
                val iHref = albumIncreasePageHref(albumDocument, bAlbum.href, index)
                val iUrl = albumIncreasePageImage(iHref, albumImageUrl, index)
                bAlbum.list.add(BImage(iHref, iUrl))
            }
        }
    }

    // 通过首页图片 href ，规律发现后续 图片href，一般情况为数字递增
    protected abstract fun albumIncreasePageImage(
        albumPageHref: String,
        albumFirstImageUrl: String,
        page: Int
    ): String

    // 通过首页，发现后续的 大图 href
    protected abstract fun albumIncreasePageHref(
        albumFirstDocument: Document,
        albumFirstHref: String,
        page: Int
    ): String

    protected abstract fun analysisAlbumPageImage(albumFirstDocument: Document): String

    protected abstract fun analysisAlbumPageNum(albumFirstDocument: Document): Int

}

abstract class AlbumPageRequest(var page: Int = 0) : AlbumRequest() {

    // 游标
    val nextPage: Int
        get() = ++page
    val prevPage: Int
        get() = if (page <= 1) 0 else --page

}