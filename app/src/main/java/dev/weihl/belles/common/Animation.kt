package dev.weihl.belles.common

import android.animation.ValueAnimator
import android.view.View

/**
 * Des:
 * 辅助动画
 *
 * @author Weihl
 * Created 2020/11/26
 */

fun translateAnimation(view: View, fromX: Float, fromY: Float, toX: Float, toY: Float) {

    val xAnim = ValueAnimator.ofFloat(fromX, toX)
    xAnim.duration = 300
    xAnim.addUpdateListener {
        val xVal = it.animatedValue
        view.x = xVal as Float
    }
    xAnim.start()

    val yAnim = ValueAnimator.ofFloat(fromY, toY)
    yAnim.duration = 300
    yAnim.addUpdateListener {
        val yVal = it.animatedValue
        view.y = yVal as Float
    }
    yAnim.start()
}

