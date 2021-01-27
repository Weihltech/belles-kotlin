package dev.weihl.belles.data.remote.request

import dev.weihl.belles.data.BAlbum
import dev.weihl.belles.data.BImage
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


/**
 * 页面数据请求与提取
 * @author Ngai
 * @since 2021/1/27
 */
abstract class AlbumRequest(var pageUrl: String, var tab: String) {

    private fun pageDocument(pageUrl: String): Document {
        return Jsoup.connect(pageUrl).get()
    }

    fun loadAlbumList(): List<BAlbum> {
        return analysisPageDocument(pageDocument(pageUrl))
    }

    abstract fun analysisPageDocument(pageDocument: Document): List<BAlbum>

    // 通过专辑连接，分析并提取专辑图片数据，并填充到专辑对象中
    fun syncAlbumDetails(bAlbum: BAlbum) {
        // 通过发现在对应规律，采用拼装方式，将图片集合起来
        val albumDocument = pageDocument(bAlbum.href)
        val pageNum = analysisAlbumPageNum(albumDocument)
        val albumCover = analysisAlbumCover(albumDocument)
        val albumCoverSimilar = analysisAlbumCoverSimilar(albumCover)
        bAlbum.list.add(BImage(bAlbum.href, albumCover))
        for (index in 2..pageNum) {
            val iHref = bAlbum.href.replace(".html", "_${index}.html")
            val iUrl = "${albumCoverSimilar}${index}.jpg"
            bAlbum.list.add(BImage(iHref, iUrl))
        }
    }

    abstract fun analysisAlbumCoverSimilar(albumDocument: String): String

    abstract fun analysisAlbumCover(albumDocument: Document): String

    abstract fun analysisAlbumPageNum(albumDocument: Document): Int

}