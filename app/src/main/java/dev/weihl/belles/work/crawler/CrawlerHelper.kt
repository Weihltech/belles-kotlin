package dev.weihl.belles.work.crawler

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.annotation.NonNull
import androidx.core.content.edit
import dev.weihl.belles.common.WorkPrefName
import dev.weihl.belles.common.WorkRuntime
import dev.weihl.belles.currDateYyyyMmDd

/**
 * @desc App 共享记录数据
 *
 * @author Weihl Created by 2020/4/29
 *
 */


private fun workPreferences(@NonNull context: Context): SharedPreferences {
    return context.getSharedPreferences(WorkPrefName, MODE_PRIVATE)
}

private fun getCrawlerMmnetWorkTime(@NonNull context: Context): String? {
    return workPreferences(context)
        .getString(WorkRuntime, "")
}

fun setCrawlerMmnetWorkTime(@NonNull context: Context, time: String) {
    return workPreferences(context).edit(commit = true) {
        putString(WorkRuntime, time)
    }
}

fun allowCrawlerMmnetWork(@NonNull context: Context): Boolean {
    val currDate = currDateYyyyMmDd()
    val workTime =
        getCrawlerMmnetWorkTime(context)
    println("currDate = $currDate ; workTime = $workTime")
    if (currDate == workTime) {
        return false
    }
    return true
}