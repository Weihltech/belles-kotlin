package dev.weihl.belles.common

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
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

fun Context.loadImage(
    imageView: ImageView,
    referer: String,
    imgUrl: String,
    callback: ((Drawable?) -> Unit)? = null
) {
    Timber.d("image load referer:$referer ; cover:$imgUrl")
    val header = LazyHeaders.Builder().addHeader("Referer", referer).build()
    val glideUrl = GlideUrl(imgUrl, header)
    Glide.with(this).load(glideUrl)
        .addListener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                if (callback != null) {
                    callback(null)
                }
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                if (callback != null) {
                    callback(resource)
                }
                return false
            }

        })
        .into(imageView)
}