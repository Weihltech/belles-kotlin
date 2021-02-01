package dev.weihl.belles.common

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager


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