package dev.weihl.belles

import android.app.Application
import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dev.weihl.belles.work.BellesWork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * @desc Application
 *
 * @author Weihl Created by 2019/11/19
 *
 */

class MainApp : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    init {
        Timber.plant(Timber.DebugTree())
        Timber.tag("MainApp")
        Timber.d("init !")

        delayedInit()
    }

    fun getContext(): Context {
        return this
    }

    private fun delayedInit() = applicationScope.launch {
        setupRecurringWork()
    }

    private fun setupRecurringWork() {
        val repeatingRequest =
            PeriodicWorkRequestBuilder<BellesWork>(1, TimeUnit.DAYS)
                .build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            BellesWork.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }

}