package dev.weihl.belles.data

import com.google.gson.Gson
import dev.weihl.belles.MainApp
import dev.weihl.belles.data.local.LocalDataSource
import dev.weihl.belles.data.local.entity.Belles
import dev.weihl.belles.data.remote.req.AlbumTab
import dev.weihl.belles.data.remote.req.SexyMm131Request

object BellesRepository : DataSource.Repository {

    private val mContext = MainApp.getContext()
    private var localDB: LocalDataSource = LocalDataSource(mContext)
    private val GSON = Gson()

    override fun loadAlbumList(tab: AlbumTab, page: Int): List<Belles> {

        when (tab) {
            AlbumTab.SEXY -> return sexyAlbumLoad(page)
        }

        return emptyList()
    }

    private fun sexyAlbumLoad(page: Int): List<Belles> {

        val sexyRequest = SexyMm131Request(page)
        val sexyAlbumList = sexyRequest.loadAlbumList()
        // 取网络数据
        val sexyBelles = mutableListOf<Belles>()
        sexyAlbumList.forEach {
            // 若有本地记录，则取本地数据
            val localBelles = localDB.queryBelles(it.href)
            if (localBelles != null) {
                sexyBelles.add(localBelles)
            } else {
                // snyc remote
                sexyRequest.syncAlbumDetails(it)
                // cover
                val newBelles = coverBells(it)
                sexyBelles.add(newBelles)
                // save album
                localDB.insertBelles(newBelles)
            }
        }

        return sexyBelles
    }

    private fun coverBells(bAlbum: BAlbum): Belles {
        return Belles(
            0,
            bAlbum.title,
            bAlbum.href,
            bAlbum.cover,
            "",
            bAlbum.tab,
            "no",
            GSON.toJson(bAlbum.list),
            System.currentTimeMillis(),
            bAlbum.href
        )
    }

    override fun markFavorites(belles: Belles) {
        if ("yes" == belles.favorite) {
            belles.favorite = "no"
        } else {
            belles.favorite = "yes"
        }
        localDB.updateBelles(belles)
    }

    override fun queryAllFavoriteBelles(): List<Belles>? {
        return localDB.queryAllFavoriteBelles()
    }


}