package dev.weihl.belles.screens

import android.os.Bundle
import android.view.View.*
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber

/**
 * @desc 抽象Activity基础类
 *
 * @author Weihl Created by 2019/11/19
 *
 */

open class BasicActivity : AppCompatActivity() {


    init {
        Timber.tag("Screen")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d(localClassName)
    }

    override fun onResume() {
        super.onResume()

        // 全面屏隐藏状态/导航栏，底部向上滑动显示状态/导航栏
        window.decorView.systemUiVisibility = (SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or SYSTEM_UI_FLAG_FULLSCREEN
                or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

}