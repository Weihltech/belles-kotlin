package dev.weihl.belles.screens.home.belles

import android.app.Application
import androidx.lifecycle.MutableLiveData
import dev.weihl.belles.data.BellesRepository
import dev.weihl.belles.data.Repository
import dev.weihl.belles.data.local.entity.Belles
import dev.weihl.belles.screens.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private var mPage = 0
    private val bellesRepository: BellesRepository

    init {
        Timber.tag("BaseViewModel")
        bellesRepository = BellesRepository(application)
    }

    val subBelles = MutableLiveData<List<Belles>>()
//    var allBelles = dao.queryAllDescId()
//
//    var lastBelles = dao.queryLastBelles()
//
//    var bellesSizeString = Transformations.map(allBelles) {
//        return@map "Belles.Size() = ${it.size}"
//    }

    fun addBellesClick() {
        uiScope.launch {
            // new Belles and Insert
            withContext(Dispatchers.IO) {
//                val belles = Belles()
//                belles.title = "title"//"Title:${allBelles.value?.size}"
//                belles.href = "Last Belles , new and insert BellesDatabase ! " +
//                        "last.index = ${allBelles.value?.size}"
//                dao.insert(belles)
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        Timber.d("onCleared !")
    }

    fun loadNextBelles() {
        netScope.launch {
            ++mPage
            Timber.d("loadNextBelles ! page = $mPage")
            bellesRepository.loadSexyDetails(mPage,
                object : Repository.CallBack {
                    override fun onResultSexyBelles(list: ArrayList<Belles>?) {
                        if (list == null || list.isEmpty()) {
                            return
                        }
                        // update event
                        uiScope.launch {
                            subBelles.value = list
                        }
                    }
                })
        }
    }

}