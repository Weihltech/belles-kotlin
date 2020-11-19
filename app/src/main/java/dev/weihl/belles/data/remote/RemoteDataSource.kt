package dev.weihl.belles.data.remote

import android.content.Context
import dev.weihl.belles.data.DataSource
import dev.weihl.belles.data.SexyImage
import dev.weihl.belles.data.SexyPage
import timber.log.Timber

class RemoteDataSource(application: Context) : DataSource.Remote {

    private val mContext = application


    init {
        Timber.tag("RemoteRepository")
    }

    override fun loadSexyPageList(page: Int): ArrayList<SexyPage> {
        try {
            return SexyRequest(mContext).loadSexyPageList(page)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return ArrayList()
    }

    override fun loadSexyImageList(sexyPage: SexyPage): ArrayList<SexyImage> {
        try {
            return SexyRequest(mContext).loadSexyPageDetailList(sexyPage)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return ArrayList()
    }
}