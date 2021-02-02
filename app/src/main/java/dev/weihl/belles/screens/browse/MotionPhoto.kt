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
    private val handler: Handler,
    private val callback: MotionCallBack
) : MotionJudge() {

    private val screenPix = MainApp.getContext().screenPixels()
    private val screenOffsetWH = arrayOf(0, 0)
    private val maskView = ImageView(MainApp.getContext()).apply {
        setBackgroundColor(Color.GRAY)
        scaleType = ImageView.ScaleType.CENTER_CROP
        layoutParams = ViewGroup.LayoutParams(0, 0)
    }

    interface MotionCallBack {
        fun maskViewRestoreFinish()

        fun maskViewAlphaChange(alpha: Float)
    }

    init {
        judgeCallBack = object : CallBack {

            private var alpha: Float = 0f
            override fun onUp() {
                if (alpha == 1f) {
                    translateAnimation(maskView, origin.x.toFloat(), origin.y.toFloat())
                    handler.postDelayed({ callback.maskViewRestoreFinish() }, 500)
                } else {
                    maskView.alpha = 0f
                    callback.maskViewAlphaChange(0f)
                }
            }

            // Y移动量，锚点 down;
            override fun onMoveVertical(offsetY: Float, moveXY: Array<Float>): Boolean {
                val tAlpha = -offsetY * 0.003f
                alpha = if (tAlpha <= 0) 0f else (if (tAlpha >= 1) 1f else tAlpha)
                Timber.d("onMoveVertical: $alpha")
                maskView.alpha = alpha

                origin.rect?.let {
                    val llp = maskView.layoutParams
                    llp.width = (screenPix[0] + screenOffsetWH[0] * alpha).toInt()
                    llp.height = (screenPix[1] + screenOffsetWH[1] * alpha).toInt()
                    maskView.layoutParams = llp

                    maskView.x = moveXY[0] - llp.width / 2
                    maskView.y = moveXY[1] - llp.height / 2

                }

                callback.maskViewAlphaChange(alpha)

                return true
            }
        }
    }

    fun maskOriginView(viewGroup: ViewGroup): ImageView? {
        maskOriginView()?.let {
            viewGroup.addView(it)
            return it
        }
        return null
    }

    private fun maskOriginView(): ImageView? {

        if (origin.rect == null) {
            return null
        }

        val rectIt = origin.rect
        // 宽高与屏幕大小
        screenOffsetWH[0] = rectIt.width() - screenPix[0]
        screenOffsetWH[1] = rectIt.height() - screenPix[1]
        // 映射到 原始 坐标
        maskView.x = origin.x.toFloat()
        maskView.y = origin.y.toFloat()
        // 映射 原始 大小
        maskView.layoutParams.width = rectIt.width()
        maskView.layoutParams.height = rectIt.height()
        maskView.alpha = 1f

        startAnimation(maskView)
        return maskView
    }

    private fun startAnimation(view: View) {
        handler.post {
            // 移动动画
            translateAnimation(view)
            // 属性放大 view
            scaleAnimation(view)
        }
        handler.postDelayed({
            alphaAnimation(view)
        }, 516)
    }

    private fun alphaAnimation(maskView: View, duration: Long = 500) {
        val rAnim = ValueAnimator.ofFloat(1f, 0f)
        rAnim.duration = duration
        rAnim.addUpdateListener { aniVal ->
            val value = aniVal.animatedValue as Float
            maskView.alpha = value
        }
        rAnim.start()
    }

    private fun scaleAnimation(maskView: View, duration: Long = 500) {
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

    private fun translateAnimation(
        maskView: View,
        toX: Float = 0f,
        toY: Float = 0f,
        duration: Long = 500
    ) {

        val xAnim = ValueAnimator.ofFloat(maskView.x, toX)
        xAnim.duration = duration
        xAnim.addUpdateListener {
            val xVal = it.animatedValue
            maskView.x = xVal as Float
        }
        xAnim.start()

        val yAnim = ValueAnimator.ofFloat(maskView.y, toY)
        yAnim.duration = duration
        yAnim.addUpdateListener {
            val yVal = it.animatedValue
            maskView.y = yVal as Float
        }
        yAnim.start()

    }

    // 原图片位置信息
    data class OriginPhoto(
        val x: Int,
        val y: Int,
        val rect: Rect?
    )
}

