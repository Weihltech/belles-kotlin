package dev.weihl.belles

import android.app.Application
import android.content.Context
import timber.log.Timber

/**
 * @desc Application
 *
 * @author Weihl Created by 2019/11/19
 *
 */
class MainApp : Application() {

    companion object {
        private lateinit var appContext: Context
        fun getAppContext(): Context {
            return appContext
        }
    }

    init {
        Timber.plant(Timber.DebugTree())
        Timber.tag("MainApp")
        Timber.d("init !")
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}