package dev.weihl.belles.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import timber.log.Timber

/**
 * @desc Work
 *
 * @author Weihl Created by 2019/12/4
 *
 */
class BellesWork(appContext: Context, params: WorkerParameters) : CoroutineWorker(
    appContext,
    params
) {

    companion object {
        const val WORK_NAME = "BellesWork"
    }

    init {
        Timber.tag("BellesWork")
    }

    override suspend fun doWork(): Result {

        Timber.d("doWork !")


    }

}