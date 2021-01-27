package dev.weihl.belles.data

/**
 * referer  图片归属，即网站会判断访问来源控制图片是否有效浏览
 * url      图片 url
 */
data class BImage(val referer: String, val url: String)

/**
 * href   专辑页面
 * title  专辑标题
 * tab    专辑项
 * cover  封面 url
 * list   专辑图片
 */
data class BAlbum(
    val href: String,
    val title: String,
    val tab: String,
    val cover: String = "",
    val list: MutableList<BImage> = mutableListOf()
)