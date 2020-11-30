package dev.weihl.belles.screens.browse

import android.animation.ValueAnimator
import android.graphics.Rect
import android.os.Handler
import android.view.View
import android.widget.RelativeLayout
import dev.weihl.belles.R
import dev.weihl.belles.common.translateAnimation
import dev.weihl.belles.databinding.ItemPhotosLayoutBinding
import timber.log.Timber

/**
 * Des:
 * 通过手势，处理图片移动与动画操作
 *
 * @author Weihl
 * Created 2020/11/26
 */

interface PhotosActionCallBack {
    fun getCurrentItemViewBind(): ItemPhotosLayoutBinding?
    fun getIndicatorView(): View?
    fun finish()
    fun getHandler(): Handler
}

var actionCallBack: PhotosActionCallBack? = null

// x, y
val centerPoint = arrayOf(0f, 0f)

var photoGlobalXY: IntArray? = null

var photoGlobalRect: Rect? = null

val touchMoveCallBack = object : TouchMoveCallBack {

    private var itemViewBind: ItemPhotosLayoutBinding? = null

    // x, y
    private val movePoint = arrayOf(0f, 0f)
    private var downTime: Long = 0L
    private var moveRatio: Float = 0f

    private fun countMoveRatio(downXY: Array<Float>, moveXY: Array<Float>) {
        val offsetY = (moveXY[1] - downXY[1]) * 0.0005f
        moveRatio = 1 - offsetY
        moveRatio = if (moveRatio < 0) 0f else if (moveRatio > 1) 1f else moveRatio
        setMoveRatio(moveRatio)
    }

    private fun setMoveRatio(moveRatio: Float) {
        // refresh param
        actionCallBack?.getIndicatorView()?.alpha = moveRatio
        itemViewBind?.image?.setTag(R.id.value, moveRatio)
        itemViewBind?.image?.scaleX = moveRatio
        itemViewBind?.image?.scaleY = moveRatio
        itemViewBind?.imageBackground?.alpha = moveRatio
    }

    private fun countMovePoint(downXY: Array<Float>, moveXY: Array<Float>) {
        movePoint[0] = moveXY[0] - centerPoint[0]
        movePoint[1] = moveXY[1] - centerPoint[1]
        setMovePoint(movePoint)
    }

    private fun setMovePoint(point: Array<Float>) {
        itemViewBind?.image?.x = point[0]
        itemViewBind?.image?.y = point[1]
    }

    override fun onDown() {
        downTime = System.currentTimeMillis()
        itemViewBind = actionCallBack?.getCurrentItemViewBind()
    }

    override fun onVerticalMove(downXY: Array<Float>, moveXY: Array<Float>) {
        if (itemViewBind == null) {
            return
        }
        countMoveRatio(downXY, moveXY)
        countMovePoint(downXY, moveXY)
    }

    override fun onUp() {
        if (System.currentTimeMillis() - downTime < 700) {
            doAnimFinish()
            return
        }
        val ratio = itemViewBind?.image?.getTag(R.id.value) as Float
        Timber.d("up . ratio $ratio ")
        if (ratio < 0.5f) {
            doAnimFinish()
            Timber.d("up . finish ")
        } else {
            setMoveRatio(1f)
            setMovePoint(arrayOf(0f, 0f))
        }
    }

    private fun doAnimFinish() {
        if (photoGlobalXY == null) {
            actionCallBack?.finish()
        } else {

            val targetX = photoGlobalXY?.get(0)?.toFloat()!!
            val targetY = photoGlobalXY?.get(1)?.toFloat()!!

            val x = itemViewBind?.image?.x
            val y = itemViewBind?.image?.y

            itemViewBind?.let {
                if (x != null && y != null) {
                    translateAnimation(it.image, x, y, targetX, targetY)
                }

//                val pxiWidth = centerPoint[0] * 2
//                val pxiHeight = centerPoint[1] * 2
//                val width = photoGlobalRect?.width()?.plus(10)
//                val height = photoGlobalRect?.height()?.plus(10)
//                val srcRadio = (pxiWidth * moveRatio).div(pxiWidth)
//                val targetRadio = width?.div(pxiWidth)
//                targetRadio?.let { tr ->
//                    val rAnim = ValueAnimator.ofFloat(srcRadio, tr)
//                    rAnim.duration = 300
//                    rAnim.addUpdateListener { aniVal ->
//                        it.image.layoutParams = RelativeLayout.LayoutParams(
//                            ((aniVal.animatedValue as Float * pxiWidth).toInt()),
//                            ((aniVal.animatedValue as Float * pxiHeight).toInt())
//                        )
//                        Timber.d("????＞＞＞＞＞＞　${it.image.layoutParams.width}")
//                    }
//                    rAnim.start()
//                }

            }

            actionCallBack?.getHandler()?.postDelayed({
                actionCallBack?.finish()
            }, 300)
        }
    }

}

fun photoEnlargeAnim() {

    Timber.d("PhotosAction  photoGlobalRect:${photoGlobalRect.toString()}")
    Timber.d("PhotosAction photoGlobalXY:${photoGlobalXY?.contentToString()}")


    // 映射到 Item缩略图 坐标
    val itemLayoutBind = actionCallBack?.getCurrentItemViewBind()
    itemLayoutBind?.image?.x = photoGlobalXY?.get(0)?.toFloat()!!
    itemLayoutBind?.image?.y = photoGlobalXY?.get(1)?.toFloat()!!

    // 映射 Item缩略图 大小
    val width = photoGlobalRect?.width()?.plus(10)
    val height = photoGlobalRect?.height()?.plus(10)
    width?.let {
        height?.let {
            itemLayoutBind?.image?.layoutParams = RelativeLayout.LayoutParams(width, height)
        }
    }

    // 移动到原点过程中，将图片放大到全屏
    actionCallBack?.getHandler()?.postDelayed({

        // 移动到原点
        itemLayoutBind?.image?.let {
            translateAnimation(
                it,
                photoGlobalXY!![0].toFloat(), photoGlobalXY!![1].toFloat(),
                0f, 0f
            )
        }

        val pxiWidth = centerPoint[0] * 2
        val pxiHeight = centerPoint[1] * 2
        val radio = width?.div(pxiWidth)
        radio?.let {
            val rAnim = ValueAnimator.ofFloat(it, 1f)
            rAnim.duration = 300
            rAnim.addUpdateListener { aniVal ->
                itemLayoutBind?.image?.layoutParams = RelativeLayout.LayoutParams(
                    ((aniVal.animatedValue as Float * pxiWidth).toInt()),
                    ((aniVal.animatedValue as Float * pxiHeight).toInt())
                )
            }
            rAnim.start()
        }
    }, 100)


}
