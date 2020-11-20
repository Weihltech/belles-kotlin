package dev.weihl.belles.data

import androidx.annotation.NonNull
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


private val gson = Gson()

/**
 * 所有性感页面集合
 */
data class BellesPage(
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
data class BellesImage(
    var referer: String,
    var url: String
)

fun sexyImageList2Json(@NonNull list: ArrayList<BellesImage>): String {
    return gson.toJson(list)
}

fun json2SexyImageList(@NonNull json: String): ArrayList<BellesImage>? {
    try {
        return Gson().fromJson(
            json,
            object : TypeToken<List<BellesImage?>?>() {}.type
        )
    } catch (ex: Exception) {
        // nothing
    }
    return null
}