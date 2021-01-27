package dev.weihl.belles.data


// 所有性感页面集合
data class BellesPage(val tab: String, val href: String, val title: String)


// 性感页面，对应的图片
data class BellesImage(val referer: String, val url: String)