package dev.weihl.belles.screens.browse

import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.Rect
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import dev.weihl.belles.MainApp
import dev.weihl.belles.common.screenPixels
import timber.log.Timber
import kotlin.math.roundToInt


/**
 * 图片的手势操作,
 * 1、初始时，从原始大小，放大到全屏
 * 2、垂直向下缩小到一定程度，回调退出；
 * @author Ngai
 * @since 2021/2/1
 */
class MotionPhoto(
    private val origin: OriginPhoto,
    private val handler: Handler
) : MotionJudge(motionCallBack) {

    private val screenPix = MainApp.getContext().screenPixels()
    private val screenCenterPoint = arrayOf(screenPix[0] / 2, screenPix[1] / 2)
    val maskView = ImageView(MainApp.getContext()).apply {
        setBackgroundColor(Color.GRAY)
        scaleType = ImageView.ScaleType.CENTER_CROP
        layoutParams = ViewGroup.LayoutParams(0, 0)
    }


    fun maskOriginView(): ImageView? {

        if (origin.rect == null) {
            return null
        }

        val rectIt = origin.rect
        // 映射到 原始 坐标
        maskView.x = origin.x.toFloat()
        maskView.y = origin.y.toFloat()
        // 映射 原始 大小
        maskView.layoutParams.width = rectIt.width()
        maskView.layoutParams.height = rectIt.height()
        maskView.alpha = 1f

        handler.post {
            // 移动动画
            translateAnimation(maskView, 500)
            // 属性放大 view
            scaleAnimation(maskView, 500)
        }
        handler.postDelayed({
            alphaAnimation(maskView, 300)
        }, 516)

        return maskView
    }

    private fun alphaAnimation(maskView: ImageView, duration: Long) {
        val rAnim = ValueAnimator.ofFloat(1f, 0f)
        rAnim.duration = duration
        rAnim.addUpdateListener { aniVal ->
            val value = aniVal.animatedValue as Float
            maskView.alpha = value
        }
        rAnim.start()
    }

    private fun scaleAnimation(maskView: View, duration: Long) {
        val radio = maskView.layoutParams.width.div(screenPix[0].toFloat())
        val rAnim = ValueAnimator.ofFloat(radio, 1f)
        rAnim.duration = duration
        rAnim.addUpdateListener { aniVal ->
            val value = aniVal.animatedValue as Float
            val llp = maskView.layoutParams
            llp.width = (value * screenPix[0]).roundToInt()
            llp.height = (value * screenPix[1]).roundToInt()
            maskView.layoutParams = llp
        }
        rAnim.start()
    }

    private fun translateAnimation(view: View, duration: Long) {

        val xAnim = ValueAnimator.ofFloat(view.x, 0f)
        xAnim.duration = duration
        xAnim.addUpdateListener {
            val xVal = it.animatedValue
            view.x = xVal as Float
        }
        xAnim.start()

        val yAnim = ValueAnimator.ofFloat(view.y, 0f)
        yAnim.duration = duration
        yAnim.addUpdateListener {
            val yVal = it.animatedValue
            view.y = yVal as Float
        }
        yAnim.start()

    }

    companion object {
        private val motionCallBack = object : CallBack {
            // Y移动量，锚点 down;
            override fun onMoveVertical(offsetY: Float): Boolean {
                Timber.d("onMoveVertical: $offsetY")
                return false
            }
        }
    }

    // 原图片位置信息
    data class OriginPhoto(
        val x: Int,
        val y: Int,
        val rect: Rect?
    )
}

