package dev.weihl.belles.screens.home.belles

import android.app.Application
import androidx.lifecycle.Transformations
import dev.weihl.belles.screens.BaseViewModel
import dev.weihl.belles.data.local.AppDatabase
import dev.weihl.belles.data.local.dao.BellesDao
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

    private lateinit var dao: BellesDao

    init {
        Timber.tag("BaseViewModel")
        Timber.d("init !")
        dao = AppDatabase.getInstance(application).bellesDao
    }

    var allBelles = dao.queryAllDescId()

    var lastBelles = dao.queryLastBelles()

    var bellesSizeString = Transformations.map(allBelles) {
        return@map "Belles.Size() = ${it.size}"
    }

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

    fun clearAllClick() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dao.deleteAll()
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        Timber.d("onCleared !")
    }

}