package dev.weihl.belles.screens.home.favorite

import android.app.Application
import androidx.lifecycle.MutableLiveData
import dev.weihl.belles.data.BellesRepository
import dev.weihl.belles.data.local.entity.Belles
import dev.weihl.belles.screens.BasicViewModel

/**
 * @desc ViewMode
 *
 * @author Weihl Created by 2019/11/22
 *
 */
class FavoriteViewModel(application: Application) : BasicViewModel(application) {

    private val repository = BellesRepository()

    val bellesList = MutableLiveData<List<Belles>>()

    fun markFavorites(itemBelles: Belles) {
        repository.markFavorite(itemBelles)
    }

    fun queryAllFavoriteBelles() {
        Thread {
            val bList = repository.queryAllFavoriteBelles()
            bList?.let {
                bellesList.postValue(it)
                return@Thread
            }
            bellesList.postValue(emptyList())
        }.start()
    }
}