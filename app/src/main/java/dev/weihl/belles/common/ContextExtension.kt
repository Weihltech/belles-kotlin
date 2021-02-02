package dev.weihl.belles.common

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import timber.log.Timber


/**
 * Context 扩展方法
 * @author Ngai
 * @since 2021/2/1
 */

fun Context.screenPixels(): Array<Int> {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val dm = DisplayMetrics()
    wm.defaultDisplay.getRealMetrics(dm)
    val width = dm.widthPixels
    val height = dm.heightPixels
    return arrayOf(width, height)
}

fun Context.loadImage(imageView: ImageView, referer: String, imgUrl: String) {
    Timber.d("image load referer:$referer ; cover:$imgUrl")
    val header = LazyHeaders.Builder().addHeader("Referer", referer).build()
    val glideUrl = GlideUrl(imgUrl, header)
    Glide.with(this).load(glideUrl).into(imageView)
}