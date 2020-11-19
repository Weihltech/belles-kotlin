package dev.weihl.belles.data

import androidx.annotation.NonNull
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


private val gson = Gson()

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

fun sexyImageList2Json(@NonNull list: ArrayList<SexyImage>): String {
    return gson.toJson(list)
}

fun json2SexyImageList(@NonNull json: String): ArrayList<SexyImage>? {
    try {
        return Gson().fromJson(
            json,
            object : TypeToken<List<SexyImage?>?>() {}.type
        )
    } catch (ex: Exception) {
        // nothing
    }
    return null
}