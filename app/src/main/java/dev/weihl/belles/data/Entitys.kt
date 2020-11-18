package dev.weihl.belles.data

/**
 * 所有性感页面集合
 */
data class SexyPage(
    val tab: String,
    val href: String,
    val title: String
) {
    override fun toString(): String {
        return "SexyPage(tab='$tab', href='$href', title='$title')"
    }
}


/**
 * 性感页面，对应的图片
 */
data class SexyImage(
    var referer: String,
    var url: String
)