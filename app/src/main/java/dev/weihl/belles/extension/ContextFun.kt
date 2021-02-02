package dev.weihl.belles.extension

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.hardware.display.DisplayManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.DisplayMetrics
import android.view.Display
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dev.weihl.belles.common.IntentKey
import dev.weihl.belles.screens.browse.PhotosActivity
import timber.log.Timber


/**
 * Context 扩展方法
 * @author Ngai
 * @since 2021/2/1
 */

fun Context.screenPixels(): Array<Int> {
    val wm = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    val dm = DisplayMetrics()
    wm.displays[Display.DEFAULT_DISPLAY].getRealMetrics(dm)
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

// details        album.list
// index          show default index
// location       mask view x,y
// rect           mask view rect in screen
// referer , url  bImage data
fun Context.startPhotosActivity(
    details: String,// belles.deatils
    index: Int = 0,
    location: IntArray? = null,
    rect: Rect? = null,
    referer: String? = null,
    url: String? = null
) {
    val photoIntent = Intent(this, PhotosActivity::class.java)
    photoIntent.putExtra(IntentKey.DETAIL, details)
    photoIntent.putExtra(IntentKey.INDEX, index)
    photoIntent.putExtra(IntentKey.LOCATION, location)
    photoIntent.putExtra(IntentKey.OBJECT_RECT, rect)
    photoIntent.putExtra(IntentKey.REFERER, referer)
    photoIntent.putExtra(IntentKey.URL, url)
    startActivity(photoIntent)

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