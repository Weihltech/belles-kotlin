package dev.weihl.belles.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color.BLACK
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import dev.weihl.belles.screens.BasicActivity
import timber.log.Timber

/**
 * @desc 流星动画视图，随机生成 Star 并执行动画
 *
 * @author Weihl Created by 2019/11/19
 */

class MeteorView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {


    companion object {
        private const val STAR_SIZE = 48
        private const val ANIM_DURATION = 3000L
        private const val LOOP_DURATION = 100L
    }

    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private lateinit var loopRunnable: Runnable

    init {
        Timber.tag("MeteorView")
        Timber.d(" Init !")
        background = ColorDrawable(BLACK)
        loopRunnable = Runnable {
            addAnimatorSet(newStar()).start()
            handler.postDelayed(loopRunnable, LOOP_DURATION)
        }
        if (context is BasicActivity) {
            context.lifecycle.addObserver(object : LifecycleObserver {

                @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
                fun onPause() {
                    loopPause()
                }

                @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
                fun onResume() {
                    loopResume()
                }

            })

        }

    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        Timber.d("MeteorView onSizeChanged !")
    }

    fun loopResume() {
        Timber.d("MeteorView loopResume !")
        handler?.postDelayed(loopRunnable, LOOP_DURATION)
    }

    fun loopPause() {
        Timber.d("MeteorView loopPause !")
        handler?.removeCallbacks(loopRunnable)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        loopResume()
    }

    private fun newStar(): AppCompatImageView {
        val scale = (Math.random() * 2.0f + .1f).toFloat()
        val newStar = AppCompatImageView(context).apply {
            layoutParams = LayoutParams(STAR_SIZE, STAR_SIZE)
            setImageResource(android.R.drawable.star_big_on)
            y = -layoutParams.height.toFloat()
            x = (Math.random() * mWidth).toFloat()
            scaleX = scale
            scaleY = scale
        }
        addView(newStar)
        return newStar
    }

    private fun addAnimatorSet(view: View): AnimatorSet {
        return AnimatorSet().apply {
            playTogether(newTranslationAnim(view), newRotationAnim(view))
            duration = ANIM_DURATION
            interpolator = AccelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    removeView(view)
                }
            })
        }
    }

    private fun newTranslationAnim(view: View): Animator? {
        return ObjectAnimator.ofFloat(
            view, View.TRANSLATION_Y,
            -100.0f,
            height + 100.0f
        )
    }

    private fun newRotationAnim(view: View): Animator? {
        return ObjectAnimator.ofFloat(
            view, View.ROTATION, 0.0f, 720.0f
        )
    }


}