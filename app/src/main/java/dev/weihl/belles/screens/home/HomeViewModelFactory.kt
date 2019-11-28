package dev.weihl.belles.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.RuntimeException

/**
 * @desc 首页 ViewModel Factory
 *
 * @author Weihl Created by 2019/11/27
 *
 */
class HomeViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(BellesViewMode::class.java)) {
            return BellesViewMode() as T
        }


        throw RuntimeException("Unknown ViewModel class")
    }

}