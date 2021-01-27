package dev.weihl.belles.data.remote.request.mm131


/**
 * 性感专辑请求
 * @author Ngai
 * @since 2021/1/27
 */
class SexyMm131Request(val page: Int) : Mm131Request() {

    init {
        pageUrl = "https://www.mm131.net/xinggan/list_6_$page.html"
        tab = "xinggan"

    }

}