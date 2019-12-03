package dev.weihl.belles.screens.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.weihl.belles.data.local.belles.BellesDBDao
import dev.weihl.belles.screens.home.belles.BellesViewModel
import java.lang.RuntimeException

/**
 * @desc 首页 ViewModel Factory
 *
 * @author Weihl Created by 2019/11/27
 *
 */
class HomeViewModelFactory(
    private val bellesDBDao: BellesDBDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(BellesViewModel::class.java)) {
            return BellesViewModel(bellesDBDao, application) as T
        }

        throw RuntimeException("Unknown ViewModel class")
    }

}