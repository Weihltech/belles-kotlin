package dev.weihl.belles.screens.browse

import android.view.MotionEvent
import timber.log.Timber
import kotlin.math.abs


/**
 * 手势判断；判断垂直或水平 motion action
 * @author Ngai
 * @since 2021/2/1
 */
open class MotionJudge(callBack: CallBack? = null) {

    private val eventDown = arrayOf(0f, 0f)
    private val eventMove = arrayOf(0f, 0f)
    private var isVerticalMotion = false
    private var hasJudged = false
    private var offsetX = 0F
    private var offsetY = 0F
    protected var judgeCallBack: CallBack? = callBack

    interface CallBack {
        fun onDown() {}

        fun onUp() {}

        // Y移动量，锚点 down;
        fun onMoveVertical(offsetY: Float, moveXY: Array<Float>): Boolean {
            return false
        }

        fun onMoveHorizontal(offsetX: Float, moveXY: Array<Float>): Boolean {
            return false
        }
    }

    // 示例：judgeTouchEvent(event) || super.dispatchTouchEvent(event)
    fun holdTouchEvent(event: MotionEvent?): Boolean {

        event?.let {

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    eventDown[0] = event.x
                    eventDown[1] = event.y
                    Timber.d("down: (${event.x},${event.y}")
                    judgeCallBack?.onDown()
                }
                MotionEvent.ACTION_UP -> {
                    hasJudged = false
                    isVerticalMotion = false
                    offsetX = 0F
                    offsetY = 0F
                    Timber.d("up: (${event.x},${event.y}")
                    judgeCallBack?.onUp()
                }
                MotionEvent.ACTION_MOVE -> {
                    eventMove[0] = event.x
                    eventMove[1] = event.y
                    // Timber.d("move: (${event.x},${event.y}")
                    offsetX = eventDown[0] - eventMove[0]
                    offsetY = eventDown[1] - eventMove[1]
                    doJudged(offsetX, offsetY)
                }
            }

            if (hasJudged) {
                return if (isVerticalMotion) {
                    judgeCallBack?.onMoveVertical(offsetY, eventMove) ?: false
                } else judgeCallBack?.onMoveHorizontal(offsetX, eventMove) ?: false
            }
        }

        return false
    }

    private fun doJudged(offsetX: Float, offsetY: Float) {
        val x = abs(offsetX)
        val y = abs(offsetY)
        if (!hasJudged) {
            Timber.d("move: （offsetX，offsetY） = (${x},${y}")
            // 单位计算 >10
            if (x > 10 || y > 10) {
                hasJudged = true
                if (y - x > 2) {
                    // 此时，垂直滑动，角度 大于 45°；
                    isVerticalMotion = true
                    Timber.d("move:  is vertical motion !")
                } else {
                    Timber.d("move:  is horizontal motion !")
                }
            }
        }
    }

}