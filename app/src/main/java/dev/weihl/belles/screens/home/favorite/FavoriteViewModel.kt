package dev.weihl.belles.screens.home.favorite

import android.app.Application
import androidx.lifecycle.MutableLiveData
import dev.weihl.belles.data.BellesRepository
import dev.weihl.belles.data.local.entity.Belles
import dev.weihl.belles.screens.BaseViewModel
import timber.log.Timber

/**
 * @desc ViewMode
 *
 * @author Weihl Created by 2019/11/22
 *
 */
class FavoriteViewModel(
    application: Application
) : BaseViewModel(application) {

    private val bellesRepository = BellesRepository

    val subBelles = MutableLiveData<List<Belles>>()
    private val allBells = ArrayList<Belles>()

    override fun onCleared() {
        super.onCleared()
        Timber.d("onCleared !")
    }

    fun markFavorites(itemBelles: Belles) {
        bellesRepository.markFavorites(itemBelles)
    }

    fun queryAllFavoriteBelles() {

    }
}