package dev.weihl.belles.extension

import androidx.annotation.NonNull
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.weihl.belles.data.BImage

/**
 * Des:
 * Gosn
 *
 * @author Weihl
 * Created 2021/2/2
 */

fun Gson.json2SexyImageList(@NonNull json: String): List<BImage> {
    runCatching {
        return fromJson(
            json,
            object : TypeToken<List<BImage?>?>() {}.type
        )
    }
    return emptyList()
}