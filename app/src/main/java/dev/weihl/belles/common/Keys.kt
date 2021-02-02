package dev.weihl.belles.common

/**
 * @desc SharedPrefence 常量
 *
 * @author Weihl Created by 2020/4/30
 *
 */
interface IntentKey {
    companion object {
        const val OBJECT_RECT = "object_rect"
        const val LOCATION = "location"
        const val DETAIL = "detail"
        const val REFERER = "referer"
        const val URL = "url"
        const val INDEX = "index"
    }
}