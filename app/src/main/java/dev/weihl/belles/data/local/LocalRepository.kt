package dev.weihl.belles.data.local

import android.content.Context
import dev.weihl.belles.data.local.entity.Belles
import timber.log.Timber

/**
 * @desc 本地 SQLite 存储库
 *
 * @author Weihl Created by 2019/11/28
 *
 */
class LocalRepository(context: Context) {


    init {
        Timber.tag("LocalRepository")
    }


    private fun Belles.toLog(): String {
        return "id:$id : title:$title"
    }

}