package dev.weihl.belles.screens.home.belles

import android.app.Application
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import dev.weihl.belles.BaseViewModel
import dev.weihl.belles.data.local.belles.Belles
import dev.weihl.belles.data.local.belles.BellesDBDao
import dev.weihl.belles.formatBelles
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*

/**
 * @desc ViewMode
 *
 * @author Weihl Created by 2019/11/22
 *
 */
class BellesViewModel(
    private val dbDao: BellesDBDao,
    application: Application
) : BaseViewModel(application) {

    init {
        Timber.tag("BaseViewModel")
        Timber.d("init !")
    }

    var allBelles = dbDao.queryAllDescId()

    var bellesSizeString = Transformations.map(allBelles) {
        return@map "Belles.Size() = ${it.size}"
    }

    fun addBellesClick() {
        uiScope.launch {
            // new Belles and Insert
            withContext(Dispatchers.IO) {
                val belles = Belles()
                belles.title = "Title:${allBelles.value?.size}"
                belles.desc = "Last Belles , new and insert BellesDatabase ! " +
                        "last.index = ${allBelles.value?.size}"
                dbDao.insert(belles)
            }
        }
    }

    fun clearAllClick(){
        uiScope.launch {
            withContext(Dispatchers.IO){
                dbDao.deleteAll()
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        Timber.d("onCleared !")
    }

}