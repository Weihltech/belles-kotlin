package dev.weihl.belles.work.crawler

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dev.weihl.belles.work.crawler.mmnet.CrawlerMmnetWork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber


/**
 * 应用后台任务管理，主要针对业务层的操作
 *
 * @author Weihl Created by 2019/12/4
 *
 */
class CrawlerWork(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        fun enqueue() {
            val coroutineScope = CoroutineScope(Dispatchers.IO)
            coroutineScope.launch {
                var appWorkRequest =
                    OneTimeWorkRequestBuilder<CrawlerWork>().build()
                WorkManager.getInstance().enqueue(appWorkRequest)
            }
        }

        const val WORK_NAME = "AppCrawlerWork"
    }

    init {
        Timber.tag("AppCrawlerWork")
    }

    override suspend fun doWork(): Result {
        Timber.d("doWork !")

        CrawlerMmnetWork(applicationContext).run()

        return Result.success()
    }

}
