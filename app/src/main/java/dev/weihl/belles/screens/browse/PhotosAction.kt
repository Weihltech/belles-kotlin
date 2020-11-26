package dev.weihl.belles.screens.browse

import android.animation.ValueAnimator
import android.graphics.Rect
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

    private fun countMoveRatio(downXY: Array<Float>, moveXY: Array<Float>) {
        val offsetY = (moveXY[1] - downXY[1]) * 0.0005f
        var moveRatio = 1 - offsetY
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

            val targetX = photoGlobalXY!![0].minus(centerPoint[0] - 300)
            val targetY = photoGlobalXY!![1].minus(centerPoint[1] - 300)

            val x = itemViewBind?.image?.x
            Timber.d("x :>># $x")
            val xValueAnimator = x?.let {
                ValueAnimator.ofFloat(it, targetX.toFloat())
            }
            xValueAnimator?.addUpdateListener {
                val value = it.animatedValue as Float
                itemViewBind?.image?.x = value
                Timber.d("x :>>@ ${itemViewBind?.image?.x}")
            }
            xValueAnimator?.duration = 400
            xValueAnimator?.start()

            val y = itemViewBind?.image?.y
            Timber.d("y :>># $y")
            val yValueAnimator = y?.let {
                ValueAnimator.ofFloat(it, targetY.toFloat())
            }
            yValueAnimator?.addUpdateListener {
                itemViewBind?.image?.y = it.animatedValue as Float
                Timber.d("y :>>@ ${itemViewBind?.image?.y}")
            }
            yValueAnimator?.duration = 400

            yValueAnimator?.start()

            actionCallBack?.getIndicatorView()?.postDelayed({
                actionCallBack?.finish()
            }, 300)
        }
    }

}

fun photoEnlargeAnim() {
    Timber.d("PhotosAction  photoGlobalRect:${photoGlobalRect.toString()}")
    Timber.d("PhotosAction photoGlobalXY:${photoGlobalXY?.contentToString()}")

    val itemLayoutBind = actionCallBack?.getCurrentItemViewBind()
    itemLayoutBind?.image?.x = photoGlobalXY?.get(0)?.toFloat()!!
    itemLayoutBind?.image?.y = photoGlobalXY?.get(1)?.toFloat()!!

    val width = photoGlobalRect?.width()
    val height = photoGlobalRect?.height()

    itemLayoutBind?.image?.layoutParams = RelativeLayout.LayoutParams(width!!, height!!)

    // 图中心 向 屏幕中心 移动
    val targetX = centerPoint[0] - width / 2
    val targetY = centerPoint[1] - height / 2
    itemLayoutBind?.image?.let {
        translateAnimation(
            it,
            photoGlobalXY!![0].toFloat(), photoGlobalXY!![1].toFloat(),
            0f, 0f
        )
    }

//    actionCallBack?.getIndicatorView()?.postDelayed({
//        itemLayoutBind?.image?.layoutParams = RelativeLayout.LayoutParams((centerPoint[0] * 2).toInt(),
//            (centerPoint[1] * 2).toInt())
//    },1000)

    // 填充整个屏幕
    itemLayoutBind?.image?.let {
        enlargeAnimation(
            it, (centerPoint[0] * 2).toInt(), (centerPoint[1] * 2).toInt()
        )
    }


    // 放大到全局

}

fun enlargeAnimation(view: View, toWith: Int, toHeight: Int) {
    val rAnim = ValueAnimator.ofFloat(0f, 1f)
    rAnim.duration = 300
    rAnim.addUpdateListener {
        val w = it.animatedValue as Float * toWith
        val h = it.animatedValue as Float * toHeight
        view.layoutParams = RelativeLayout.LayoutParams(
            w.toInt(),
            h.toInt()
        )
    }
    rAnim.start()
}
