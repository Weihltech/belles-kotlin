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

    private var title: String = "???"


    private var allBelles = dbDao.queryAllDescId()

    val allBellesString = Transformations.map(allBelles) {
        formatBelles(it)
    }


    fun onClick() {
        uiScope.launch {
            title = getTitle()
            _count.value = _count.value?.plus(1)
            Timber.d("onClick !" + count.value)
        }
    }

    private suspend fun getTitle(): String {
        return withContext(Dispatchers.IO) {
            val belles = Belles()
            belles.title = "title : " + _count.value
            dbDao.insert(belles)
            val title = dbDao?.queryLastBelles()?.title.toString()
            title
        }
    }

    companion object {
        const val DELAY = 1000L
        const val PERIOD = 1000L
    }

    private val _count = MutableLiveData<Int>()
    val count: LiveData<Int>
        get() = _count

    val currentTimeString = Transformations.map(count) { time ->

        val time = DateUtils.formatElapsedTime(time.toLong())

        return@map "$title : $time"
    }

    private val _timer = Timer()
//    private val _handler = Handler()

    init {
        Timber.tag("BaseViewModel")
        Timber.d("init !")
        _count.postValue(1)

        // startTime
//        _timer.scheduleAtFixedRate(object : TimerTask() {
//            override fun run() {
//                _handler.post {
//                    _count.value = _count.value?.plus(1)
//                    Timber.d(count.value.toString())
//                }
//            }
//
//        }, DELAY, PERIOD)
    }


    override fun onCleared() {
        super.onCleared()
        Timber.d("onCleared !")
        _timer.cancel()
    }

}