package dev.weihl.belles.screens.browse

import android.view.MotionEvent
import androidx.annotation.NonNull
import timber.log.Timber
import kotlin.math.abs

/**
 * 垂直手势辅助判断
 */

private val eventDown = arrayOf(0f, 0f)
private val eventMove = arrayOf(0f, 0f)
private var verticalSliding = false
private var hasJudgeSliding = false
fun judgeTouchEvent(event: MotionEvent?, @NonNull callBack: TouchMoveCallBack): Boolean {

    when (event?.action) {
        MotionEvent.ACTION_DOWN -> {
            eventDown[0] = event.x
            eventDown[1] = event.y
            callBack.onDown()
            Timber.d("ACTION_DOWN ${eventDown.contentToString()}")
        }
        MotionEvent.ACTION_UP -> {
            hasJudgeSliding = false
            verticalSliding = false
            Timber.d("ACTION_UP   ")
            callBack.onUp()
        }
        MotionEvent.ACTION_MOVE -> {
            eventMove[0] = event.x
            eventMove[1] = event.y

            if (!hasJudgeSliding) {
                val offsetX = abs(eventDown[0] - eventMove[0])
                val offsetY = abs(eventDown[1] - eventMove[1])

                Timber.d("ACTION_MOVE ${eventMove.contentToString()} ； [ offsetX：$offsetX , offsetY:$offsetY ]")
                // 单位计算 >10
                if (offsetX > 10 || offsetY > 10) {
                    hasJudgeSliding = true
                    if (offsetY - offsetX > 2) {
                        // 此时，垂直滑动，角度 大于 45°；
                        Timber.d("ACTION_MOVE  is 垂直滑动 !")
                        verticalSliding = true
                    }
                }
            }

        }
    }

    if (hasJudgeSliding && verticalSliding) {
        Timber.d("ACTION_MOVE(垂直控制) :${eventMove.contentToString()}")
        callBack.onVerticalMove(eventDown, eventMove)
        return true
    }

    return false
}

interface TouchMoveCallBack {
    fun onVerticalMove(downXY: Array<Float>, moveXY: Array<Float>) {

    }

    fun onDown() {

    }

    fun onUp() {

    }
}
