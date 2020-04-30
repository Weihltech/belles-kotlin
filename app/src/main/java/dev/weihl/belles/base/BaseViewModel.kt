package dev.weihl.belles.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

/**
 * @desc 项目 ViewModel 基类，支持对Database 等IO操作的耗时操作，原理：协程（进程>线程>协程）
 *
 * @author Weihl Created by 2019/12/3
 *
 */
abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {


    private var viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


//    fun onClick() {
//        uiScope.launch {
//            title = getTitle()
//        }
//    }
//
//    private suspend fun getTitle(): String {
//        return withContext(Dispatchers.IO) {
//            val title = dbDao?.query()?.title.toString()
//            title
//        }
//    }

}