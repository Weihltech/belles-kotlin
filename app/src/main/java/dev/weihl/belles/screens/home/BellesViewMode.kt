package dev.weihl.belles.screens.home

import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import timber.log.Timber
import java.util.*

/**
 * @desc ViewMode
 *
 * @author Weihl Created by 2019/11/22
 *
 */
class BellesViewMode : ViewModel() {
    fun clickStartTimer() {
        _count.value = _count.value?.plus(1)
        Timber.d("clickStartTimer !" + count.value)
    }

    fun clickStartTimer2() {
        Timber.d("clickStartTimer2 !" + count.value)
    }


    companion object {
        const val DELAY = 1000L
        const val PERIOD = 1000L
    }

    private val _count = MutableLiveData<Int>()
    val count: LiveData<Int>
        get() = _count

    val currentTimeString = Transformations.map(count) { time ->
        DateUtils.formatElapsedTime(time.toLong())
    }

    private val _timer = Timer()
//    private val _handler = Handler()

    init {
        Timber.tag("BellesViewMode")
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