package dev.weihl.belles.screens.browse

import android.graphics.Color
import android.graphics.Rect
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import dev.weihl.belles.MainApp
import dev.weihl.belles.common.screenPixels
import timber.log.Timber


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
    val maskView = View(MainApp.getContext()).apply {
        setBackgroundColor(Color.GRAY)
        layoutParams = ViewGroup.LayoutParams(0, 0)
    }


    fun holdOriginView() {

        if (origin.rect == null) {
            return
        }

        val rectIt = origin.rect
        // 映射到 原始 坐标
        maskView.x = origin.x.toFloat()
        maskView.y = origin.y.toFloat()
        // 映射 原始 大小
        maskView.layoutParams.width = rectIt.width()
        maskView.layoutParams.height = rectIt.height()

//        handler.postDelayed({
//            // 移动动画
//            translateAnimation(
//                targetView,
//                targetView.x, targetView.y,
//                0f, 0f
//            )
//            // 属性放大 view
//            val radio = rectIt.width().div(screenPix[0])
//            val rAnim = ValueAnimator.ofFloat(radio.toFloat(), 1f)
//            rAnim.duration = 300
//            rAnim.addUpdateListener { aniVal ->
//
//                targetView.layoutParams.width =
//                    ((aniVal.animatedValue as Float * screenPix[0]).toInt())
//                targetView.layoutParams.height =
//                    ((aniVal.animatedValue as Float * screenPix[1]).toInt())
//
////                targetView.layoutParams = ViewGroup.LayoutParams(
////                    ((aniVal.animatedValue as Float * screenPix[0]).toInt()),
////                    ((aniVal.animatedValue as Float * screenPix[1]).toInt())
////                )
//            }
//            rAnim.start()
//        }, 300)
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

