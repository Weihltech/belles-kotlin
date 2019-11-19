package dev.weihl.belles.screens

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber

/**
 * @desc 抽象Activity基础类
 *
 * @author Weihl Created by 2019/11/19
 *
 */

open class BasicActivity : AppCompatActivity(){


    init {
        Timber.tag("Screen")
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        Timber.d(localClassName)
    }

}