package dev.weihl.belles

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.text.HtmlCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.weihl.belles.data.BImage
import dev.weihl.belles.data.local.entity.Belles
import java.text.SimpleDateFormat
import java.util.*


/**
 * @desc 通用工具类
 *
 * @author Weihl Created by 2019/12/3
 *
 */

@SuppressLint("SimpleDateFormat")
private val DATE_FORMAT = SimpleDateFormat("YYYY-MM-dd")
private val GSON = Gson()

fun formatBelles(allBelles: List<Belles>): Spanned {

    val sb = StringBuilder()

    allBelles.forEach {
        sb.append("id:${it.id}; ")
        sb.append("title:${it.title}; ")
        sb.append("desc:${it.href}; ")
        sb.append("</br>")
    }

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}

fun currDateYyyyMmDd(): String {
    return DATE_FORMAT.format(Date())
}

fun Context.drawableResources(@DrawableRes resId: Int): Drawable? {
    return AppCompatResources.getDrawable(this, resId)
}

/* 网络已连接，判断连接方式 */
fun Context.isNetworkAvailable(): Boolean {
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities =
            manager.getNetworkCapabilities(manager.activeNetwork)
        if (networkCapabilities != null) {
            return (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
        }
    } else {
        val networkInfo = manager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
    return false
}

fun Context.dp2Px(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

fun Context.px2Dp(px: Int): Int {
    val scale = resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}

//fun sexyImageList2Json(@NonNull list: ArrayList<BellesImage>): String {
//    return GSON.toJson(list)
//}
//
fun json2SexyImageList(@NonNull json: String): List<BImage> {
    runCatching {
        return GSON.fromJson(
            json,
            object : TypeToken<List<BImage?>?>() {}.type
        )
    }
    return emptyList()
}