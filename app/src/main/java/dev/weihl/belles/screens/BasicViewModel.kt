package dev.weihl.belles.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel

/**
 * @desc 项目 ViewModel 基类，支持对Database 等IO操作的耗时操作，原理：协程（进程>线程>协程）
 *
 * @author Weihl Created by 2019/12/3
 *
 */
abstract class BasicViewModel(application: Application) : AndroidViewModel(application)