package dev.weihl.belles.data

import android.content.Context
import androidx.annotation.NonNull
import com.google.gson.Gson
import dev.weihl.belles.data.local.LocalDataSource
import dev.weihl.belles.data.local.entity.Belles
import dev.weihl.belles.data.remote.RemoteDataSource
import dev.weihl.belles.isNetworkAvailable
import timber.log.Timber

class BellesRepository(application: Context) : Repository {

    private val mContext = application
    private var localDataSource: LocalDataSource
    private var remoteDataSource: RemoteDataSource

    init {
        Timber.tag("BellesRepository")

        localDataSource = LocalDataSource(application)
        remoteDataSource = RemoteDataSource(application)

    }

    override fun loadSexyDetails(page: Int, @NonNull callBack: Repository.CallBack) {

        // not net work ；query all belles return
        if (!isNetworkAvailable(mContext)) {
            callBack.onResultSexyBelles(localDataSource.queryAllBelles())
            return
        }

        // load page list ,group
        Timber.d("loadSexyPageList !")
        val sexyPageList = remoteDataSource.loadSexyPageList(page)
        if (sexyPageList.isEmpty()) {
            callBack.onResultSexyBelles(null)
            Timber.d("loadSexyPageList is Null")
            return
        }

        // create new belles list
        val bellesList = ArrayList<Belles>()
        for (sexyPage in sexyPageList) {
            // read local belles
            val localBelless = localDataSource.queryBelles(sexyPage)
            if (localBelless != null) {
                bellesList.add(localBelless)
                Timber.d("loadSexyPageList has local Belles !")
                continue
            }
            // load remote
            Timber.d("loadSexyPageList load remote Belles !")
            val sexyImageList = remoteDataSource.loadSexyImageList(sexyPage)
            if (sexyImageList.isEmpty()) {
                continue
            }
            // 构建 Belles ,保存，并加入列表
            val remoteBelles = Belles(
                0,
                sexyPage.title,
                sexyPage.href,
                sexyImageList[0].url,
                "120@120",
                sexyPage.tab,
                "no",
                Gson().toJson(sexyImageList),
                System.currentTimeMillis(),
                sexyPage.href
            )
            bellesList.add(remoteBelles)
            Timber.d("loadSexyPageList add remote Belles !")
            // insert belles
            localDataSource.insertBelles(remoteBelles)
            Timber.d("loadSexyPageList insert remote Belles !")
        }

        callBack.onResultSexyBelles(bellesList)
    }

}