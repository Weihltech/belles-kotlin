package dev.weihl.belles.data.local

import android.content.Context
import dev.weihl.belles.data.DataSource
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

    override fun insertBelles(belles: Belles) {
        Timber.d("insert@ $belles")
        bellesDao().insert(belles)
    }

    override fun updateBelles(belles: Belles) {
        val localBelles = queryBellesByHref(belles.href)
        Timber.d("update@ ${belles.title} ;local:${localBelles?.title}")
        if (localBelles != null) {
            localBelles.favorite = belles.favorite
            localBelles.date = System.currentTimeMillis()
            localBelles.details = belles.details
            localBelles.href = belles.href
            localBelles.tab = belles.tab
            localBelles.thumb = belles.thumb
            localBelles.title = belles.title
            localBelles.thumbWh = belles.thumbWh
            bellesDao().update(localBelles)
        }

    }

    override fun queryAllFavoriteBelles(): List<Belles>? {
        Timber.d("queryAllFavoriteBelles@ ")
        return bellesDao().queryAllFavoriteBelles(1)
    }

    override fun queryBellesByHref(href: String): Belles? {
        return bellesDao().queryBellesByHref(href)
    }

    private fun bellesDao(): BellesDao {
        return AppDatabase.getInstance(mContext).bellesDao
    }

}