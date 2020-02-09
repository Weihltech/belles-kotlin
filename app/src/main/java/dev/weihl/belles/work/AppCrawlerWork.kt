package dev.weihl.belles.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import timber.log.Timber


/**
 * @desc Work,搜索网络资源，并插入数据库
 *
 * @author Weihl Created by 2019/12/4
 *
 */
class AppCrawlerWork(appContext: Context, params: WorkerParameters) : CoroutineWorker(
    appContext,
    params
) {

    companion object {
        const val WORK_NAME = "AppCrawlerWork"
    }

    init {
        Timber.tag("AppCrawlerWork")
    }

    override suspend fun doWork(): Result {
        Timber.d("doWork !")

        MmnetTaskWork(applicationContext).run()

        return Result.success()
    }

}
