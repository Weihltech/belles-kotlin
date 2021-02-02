package dev.weihl.belles.screens.home.belles

import android.app.Application
import androidx.lifecycle.MutableLiveData
import dev.weihl.belles.data.BellesRepository
import dev.weihl.belles.data.local.entity.Belles
import dev.weihl.belles.data.remote.EnumAlbum
import dev.weihl.belles.extension.isNetworkAvailable
import dev.weihl.belles.screens.BaseViewModel

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
    private var anEnum: EnumAlbum = EnumAlbum.SEXY

    // 页面数据
    private val _bellesListMap = HashMap<EnumAlbum, MutableList<Belles>>()
    val bellesList = MutableLiveData<List<Belles>>()

    fun switchAlbumTab(enumAlbum: EnumAlbum): Boolean {
        anEnum = enumAlbum

        // set empty list
        if (_bellesListMap[anEnum].isNullOrEmpty()) {
            _bellesListMap[anEnum] = mutableListOf()
            return false
        } else {
            bellesList.postValue(_bellesListMap[anEnum])
        }
        return true
    }

    fun loadNextBelles() {

        if (!context.isNetworkAvailable()) {
            return
        }

        Thread {
            val pageBellesList = repository.nextAlbumList(anEnum)
            if (pageBellesList.isNotEmpty()) {
                // update event,标记上次看到这里
                _bellesListMap[anEnum]?.let {
                    if (it.isNotEmpty()) {
                        it[0].date = -1L
                    }
                    it.addAll(0, pageBellesList)
                }
            }
            bellesList.postValue(_bellesListMap[anEnum])
        }.start()
    }

    fun markFavorites(itemBelles: Belles) {
        repository.markFavorite(itemBelles)
    }

}