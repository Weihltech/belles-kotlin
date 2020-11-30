package dev.weihl.belles.screens.browse

import android.animation.ValueAnimator
import android.graphics.Rect
import android.os.Handler
import android.view.View
import android.widget.RelativeLayout
import dev.weihl.belles.R
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
var pxiWidth: Float = 0f
var pxiHeight: Float = 0f

var photoGlobalXY: IntArray? = null
var photoGlobalX = 0f
var photoGlobalY = 0f
var photoGlobalRect: Rect? = null
var photoGlobalWidth = 0
var photoGlobalHeight = 0

val touchMoveCallBack = object : TouchMoveCallBack {

    private var itemViewBind: ItemPhotosLayoutBinding? = null

    // x, y
    private val movePoint = arrayOf(0f, 0f)
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
        itemViewBind?.imageBackground?.alpha = moveRatio

        val width = (pxiWidth * moveRatio).toInt()
        val height = (pxiHeight * moveRatio).toInt()
        itemViewBind?.image?.layoutParams = RelativeLayout.LayoutParams(width, height)

    }

    private fun countMovePoint(downXY: Array<Float>, moveXY: Array<Float>) {
        //  (centerPoint[0] * (1 - moveRatio)).toInt() 补偿 layoutParams缩小后的移动距离
        movePoint[0] = moveXY[0] - centerPoint[0] + (centerPoint[0] * (1 - moveRatio)).toInt()
        movePoint[1] = moveXY[1] - centerPoint[1] + (centerPoint[1] * (1 - moveRatio)).toInt()
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
        if (ratio < 0.6f) {
            doAnimFinish(itemViewBind)
            Timber.d("up . finish ")
        } else {
            setMoveRatio(1f)
            setMovePoint(arrayOf(0f, 0f))
        }
    }

}

fun doAnimFinish(itemViewBind: ItemPhotosLayoutBinding?) {
    val x = itemViewBind?.image?.x
    val y = itemViewBind?.image?.y

    itemViewBind?.let {
        if (x != null && y != null) {
            translateAnimation(it.image, x, y, photoGlobalX, photoGlobalY)
        }

        it.image.layoutParams =
            RelativeLayout.LayoutParams(photoGlobalWidth, photoGlobalHeight)

        // TODO
//        val whAnim = ValueAnimator.ofFloat(1f, photoGlobalWidth / pxiWidth)
//        whAnim.duration = 300
//        whAnim.addUpdateListener { whIt ->
//            val whRadio = whIt.animatedValue as Float
//            it.image.layoutParams = RelativeLayout.LayoutParams(
//                (pxiWidth * whRadio).toInt(), (pxiHeight * whRadio).toInt()
//            )
//        }
//        whAnim.start()


//        val widthAnim = ValueAnimator.ofInt(it.image.layoutParams.width, photoGlobalWidth)
//        widthAnim.duration = 300
//        widthAnim.addUpdateListener { widthVal ->
//            val w = widthVal.animatedValue as Int
//            it.image.layoutParams = RelativeLayout.LayoutParams(w, it.image.height)
//        }
//        widthAnim.start()
//
//        val heightAnim = ValueAnimator.ofInt(it.image.layoutParams.height, photoGlobalHeight)
//        heightAnim.duration = 300
//        heightAnim.addUpdateListener { heightVal ->
//            val h = heightVal.animatedValue as Int
//            it.image.layoutParams = RelativeLayout.LayoutParams(it.image.width, h)
//        }
//        heightAnim.start()

        actionCallBack?.getHandler()?.postDelayed({ actionCallBack?.finish() }, 316)

    }
}

fun photoEnlargeAnim() {

    Timber.d("PhotosAction  photoGlobalRect:${photoGlobalRect.toString()}")
    Timber.d("PhotosAction photoGlobalXY:${photoGlobalXY?.contentToString()}")

    // 映射到 Item缩略图 坐标
    val itemLayoutBind = actionCallBack?.getCurrentItemViewBind()
    itemLayoutBind?.image?.x = photoGlobalX
    itemLayoutBind?.image?.y = photoGlobalY

    // 映射 Item缩略图 大小
    itemLayoutBind?.image?.layoutParams =
        RelativeLayout.LayoutParams(photoGlobalWidth, photoGlobalHeight)

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

        val radio = photoGlobalWidth.div(pxiWidth)
        val rAnim = ValueAnimator.ofFloat(radio, 1f)
        rAnim.duration = 300
        rAnim.addUpdateListener { aniVal ->
            itemLayoutBind?.image?.layoutParams = RelativeLayout.LayoutParams(
                ((aniVal.animatedValue as Float * pxiWidth).toInt()),
                ((aniVal.animatedValue as Float * pxiHeight).toInt())
            )
        }
        rAnim.start()

    }, 300)

}

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
