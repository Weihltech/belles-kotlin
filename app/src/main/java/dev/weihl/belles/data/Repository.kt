package dev.weihl.belles.data

import androidx.annotation.NonNull
import dev.weihl.belles.data.local.entity.Belles

/**
 * 对外引用
 */
interface Repository {

    interface CallBack {
        fun onResultSexyBelles(list: ArrayList<Belles>?)
    }

    fun loadSexyDetails(page: Int, @NonNull callBack: CallBack)

}