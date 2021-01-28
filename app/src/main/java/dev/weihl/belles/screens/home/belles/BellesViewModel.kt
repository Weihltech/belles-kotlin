package dev.weihl.belles.screens.home.belles

import android.app.Application
import androidx.lifecycle.MutableLiveData
import dev.weihl.belles.data.BellesRepository
import dev.weihl.belles.data.local.entity.Belles
import dev.weihl.belles.data.remote.req.AlbumTab
import dev.weihl.belles.isNetworkAvailable
import dev.weihl.belles.screens.BaseViewModel
import timber.log.Timber

/**
 * @desc ViewMode
 *
 * @author Weihl Created by 2019/11/22
 *
 */
class BellesViewModel(application: Application) : BaseViewModel(application) {

    private val repository = BellesRepository
    private val context = application

    // 专辑项
    private var tab: AlbumTab = AlbumTab.SEXY

    // 页面
    private var _page = 0
    private val page: Int
        get() = ++_page

    // 页面数据
    private val _bellesList = ArrayList<Belles>()
    val bellesList = MutableLiveData<List<Belles>>()

    fun switchAlbumTab(albumTab: AlbumTab) {
        tab = albumTab
        _page = 0
        loadNextBelles()

    }

    fun loadNextBelles() {

        if (!context.isNetworkAvailable()) {
            return
        }

        Timber.d("loadNextBelles ! page = $page")

        Thread {
            val pageBellesList = repository.loadAlbumList(AlbumTab.SEXY, _page)
            if (pageBellesList.isNotEmpty()) {

                // update event,标记上次看到这里
                if (_bellesList.isNotEmpty()) {
                    _bellesList[0].date = -1L
                }

                _bellesList.addAll(0, pageBellesList)
            }
            bellesList.postValue(_bellesList)
        }.start()
    }

    fun markFavorites(itemBelles: Belles) {
        repository.markFavorites(itemBelles)
    }

}