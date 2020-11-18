package dev.weihl.belles.screens.home.favorite

import android.app.Application
import androidx.lifecycle.MutableLiveData
import dev.weihl.belles.data.BellesRepository
import dev.weihl.belles.data.Repository
import dev.weihl.belles.data.local.entity.Belles
import dev.weihl.belles.screens.BaseViewModel
import kotlinx.coroutines.launch
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

    private val bellesRepository: BellesRepository
    private val context = application

    init {
        Timber.tag("BaseViewModel")
        bellesRepository = BellesRepository(application)
    }

    val subBelles = MutableLiveData<List<Belles>>()
    private val allBells = ArrayList<Belles>()

    override fun onCleared() {
        super.onCleared()
        Timber.d("onCleared !")
    }

    fun markFavorites(itemBelles: Belles) {
        ioScope.launch {
            bellesRepository.markFavorites(itemBelles)
        }
    }

    fun queryAllFavoriteBelles() {
        ioScope.launch {
            allBells.clear()
            bellesRepository.queryAllFavoriteBelles(object : Repository.CallBack {
                override fun onResult(list: ArrayList<Belles>?) {
                    if (list != null) {
                        uiScope.launch {
                            allBells.addAll(list)
                            subBelles.value = allBells
                        }
                    }
                }

            })
        }
    }
}