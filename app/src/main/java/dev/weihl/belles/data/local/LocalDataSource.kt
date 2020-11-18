package dev.weihl.belles.data.local

import android.content.Context
import dev.weihl.belles.data.DataSource
import dev.weihl.belles.data.SexyPage
import dev.weihl.belles.data.local.dao.BellesDao
import dev.weihl.belles.data.local.entity.Belles
import timber.log.Timber

/**
 * @desc 本地 SQLite 存储库
 *
 * @author Weihl Created by 2019/11/28
 *
 */
class LocalDataSource(context: Context) : DataSource.Local {

    private val mContext = context;

    init {
        Timber.tag("LocalRepository")
    }

    override fun hasPageDetail(sexyPage: SexyPage) {
        TODO("Not yet implemented")
    }

    override fun queryBelles(sexyPage: SexyPage): Belles? {
        return bellesDao().queryBellesByHref(sexyPage.href)
    }

    override fun insertBelles(belles: Belles) {
        bellesDao().insert(belles)
    }

    private fun bellesDao(): BellesDao {
        return AppDatabase.getInstance(mContext).bellesDao
    }

}