package dev.weihl.belles.data

import androidx.annotation.NonNull
import dev.weihl.belles.data.local.entity.Belles

/**
 * 对外引用
 */
interface Repository {

    interface CallBack {
        fun onResult(list: ArrayList<Belles>?)
    }

    fun loadSexyBellesList(page: Int, @NonNull callBack: CallBack)

    fun markFavorites(belles: Belles)

    fun queryAllFavoriteBelles(@NonNull callBack: CallBack)
}