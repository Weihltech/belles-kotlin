package dev.weihl.belles.data.remote

import android.content.Context
import dev.weihl.belles.data.BellesImage
import dev.weihl.belles.data.BellesPage
import dev.weihl.belles.data.DataSource
import dev.weihl.belles.data.remote.request.SexyRequest
import timber.log.Timber

class RemoteDataSource(application: Context) : DataSource.Remote {

    private val mContext = application


    init {
        Timber.tag("RemoteRepository")
    }

    override fun loadSexyPageList(page: Int): ArrayList<BellesPage> {
        try {
            return SexyRequest(mContext).loadPageList(page)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return ArrayList()
    }

    override fun loadSexyImageList(bellesPage: BellesPage): ArrayList<BellesImage> {
        try {
            return SexyRequest(mContext).loadPageImageList(bellesPage)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return ArrayList()
    }
}