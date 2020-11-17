package dev.weihl.belles.work.bean

/**
 * @desc 爬虫数据Bean
 *
 * @author Weihl Created by 2019/12/5
 *
 */
data class WorkBelles(
    val href: String,
    var title: String,
    var thumb: String,
    var thumbWh: String,
    var tab: String,// qingchun,xinggan,xiaoyuan
    var details: String,// json
    var referer: String// referer
) {
    override fun toString(): String {
        return "WorkBelles(href='$href', title='$title', thumb='$thumb', thumbWh='$thumbWh', tab='$tab', details='$details', referer='$referer')"
    }
}

data class WorkBellesImg(

    var referer: String,
    var url: String

) {
    override fun toString(): String {
        return "WorkBellesImg(referer='$referer', url='$url')"
    }
}