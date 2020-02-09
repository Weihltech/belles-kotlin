package dev.weihl.belles.work.bean

/**
 * @desc 图片URL  包含 referer 信息；网站可能添加此信息与防止外链访问
 *
 * @author Weihl Created by 2019/12/5
 *
 */
data class WorkExtraImg(

    var referer: String,
    var url: String

)