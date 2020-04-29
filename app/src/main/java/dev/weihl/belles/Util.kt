package dev.weihl.belles

import android.annotation.SuppressLint
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import dev.weihl.belles.data.local.entity.Belles
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

/**
 * @desc 通用工具类
 *
 * @author Weihl Created by 2019/12/3
 *
 */

@SuppressLint("SimpleDateFormat")
val DATE_FORMAT = SimpleDateFormat("YYYY-MM-dd")

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
