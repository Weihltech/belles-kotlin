package dev.weihl.belles.screens.home.belles

import android.app.Application
import androidx.lifecycle.MutableLiveData
import dev.weihl.belles.data.BellesRepository
import dev.weihl.belles.data.local.entity.Belles
import dev.weihl.belles.data.remote.req.AlbumTab
import dev.weihl.belles.isNetworkAvailable
import dev.weihl.belles.screens.BaseViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * @desc ViewMode
 *
 * @author Weihl Created by 2019/11/22
 *
 */
class BellesViewModel(
    application: Application
) : BaseViewModel(application) {

    private var page = 0
    private val bellesRepository = BellesRepository
    private val context = application


    val subBelles = MutableLiveData<List<Belles>>()
    private val allBells = ArrayList<Belles>()
//    var allBelles = dao.queryAllDescId()
//
//    var lastBelles = dao.queryLastBelles()
//
//    var bellesSizeString = Transformations.map(allBelles) {
//        return@map "Belles.Size() = ${it.size}"
//    }

    override fun onCleared() {
        super.onCleared()
        Timber.d("onCleared !")
    }

    fun loadNextBelles() {
        ioScope.launch {
            if (isNetworkAvailable(context)) {
                ++page
                if (page == 1) {
                    // 连通网络后，属于第一页时，清空可能存在的本地数据列表
                    allBells.clear()
                }
            } else {
                // 断网后，清空列表，加载所有列表
                allBells.clear()
            }
            Timber.d("loadNextBelles ! page = $page")

            Thread {
                val bellesList = bellesRepository.loadAlbumList(AlbumTab.SEXY, 1)
                if (bellesList.isNotEmpty()) {
                    // update event
                    if (allBells.isNotEmpty()) {
                        allBells[0].date = -1L
                    }

                    allBells.addAll(0, bellesList)
                    uiScope.launch {
                        subBelles.value = allBells
                    }
                }
            }.start()

        }
    }

    fun defaultNextBelles() {
        if (allBells.size <= 0) {
            loadNextBelles()
        } else {
            uiScope.launch {
                subBelles.value = allBells
            }
        }

    }

    fun markFavorites(itemBelles: Belles) {
        ioScope.launch {
            bellesRepository.markFavorites(itemBelles)
        }
    }

}