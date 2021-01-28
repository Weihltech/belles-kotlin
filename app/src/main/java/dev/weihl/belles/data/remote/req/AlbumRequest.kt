package dev.weihl.belles.data.remote.req

import dev.weihl.belles.data.BAlbum
import dev.weihl.belles.data.BImage
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


/**
 * 页面数据请求与提取
 * @author Ngai
 * @since 2021/1/27
 */
sealed class AlbumRequest() {

    abstract val pageUrl: String

    abstract val tab: String

    protected fun pageDocument(pageUrl: String): Document {
        println(pageUrl)
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
            val albumDocument = pageDocument(bAlbum.href)
            val pageNum = analysisAlbumPageNum(albumDocument)
            val albumCover = analysisAlbumCover(albumDocument)
            bAlbum.list.add(BImage(bAlbum.href, albumCover))
            for (index in 2..pageNum) {
                val iHref = albumPageHref(albumDocument, bAlbum.href, index)
                val iUrl = albumPageImage(iHref, albumCover, index)
                bAlbum.list.add(BImage(iHref, iUrl))
            }
        }
    }

    protected abstract fun albumPageImage(
        albumPageHref: String,
        albumCover: String,
        index: Int
    ): String

    protected abstract fun albumPageHref(albumDocument: Document, href: String, page: Int): String

    protected abstract fun analysisAlbumCover(albumDocument: Document): String

    protected abstract fun analysisAlbumPageNum(albumDocument: Document): Int

}

abstract class AlbumPageRequest(var page: Int = 0) : AlbumRequest() {

    // 游标
    val nextPage: Int
        get() = ++page
    val prevPage: Int
        get() = if (page <= 1) 0 else --page

}