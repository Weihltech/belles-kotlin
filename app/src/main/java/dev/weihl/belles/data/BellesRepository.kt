package dev.weihl.belles.data

import com.google.gson.Gson
import dev.weihl.belles.MainApp
import dev.weihl.belles.data.local.LocalDataSource
import dev.weihl.belles.data.local.entity.Belles
import dev.weihl.belles.data.remote.*
import timber.log.Timber

class BellesRepository : DataSource.Repository {

    private val mContext = MainApp.getContext()
    private var localDB: LocalDataSource = LocalDataSource(mContext)
    private val GSON = Gson()
    private val albumRequestMap = HashMap<String, AlbumPageRequest>()

    override fun nextAlbumList(anEnum: EnumAlbum): List<Belles> {
        return loadAlbumList(anEnum, findAlbumRequest(anEnum).nextPage)
    }

    override fun loadAlbumList(anEnum: EnumAlbum, page: Int): List<Belles> {
        val albumRequest = findAlbumRequest(anEnum)
        albumRequest.page = page

        val sexyAlbumList = albumRequest.loadAlbumList()
        // 取网络数据
        val sexyBelles = mutableListOf<Belles>()
        sexyAlbumList.forEach {
            // 若有本地记录，则取本地数据
            val localBelles = localDB.queryBellesByHref(it.href)
            if (localBelles != null) {
                Timber.d(localBelles.toString())
                sexyBelles.add(0, localBelles)
            } else {
                // 并避免访问过快被拒绝访问 snyc remote；
                if (anEnum == EnumAlbum.ART || anEnum == EnumAlbum.CLASSIC) {
                    Thread.sleep(2000)
                }
                albumRequest.syncAlbumDetails(it)
                // cover
                val newBelles = coverBells(it)
                sexyBelles.add(0, newBelles)
                // save album
                localDB.insertBelles(newBelles)
            }
        }

        return sexyBelles
    }

    private fun findAlbumRequest(anEnum: EnumAlbum): AlbumPageRequest {
        var request = albumRequestMap[anEnum.tab]
        if (request == null) {
            request = selectAlbumRequest(anEnum)
            albumRequestMap[anEnum.tab] = request
        }
        return request
    }

    private fun selectAlbumRequest(anEnum: EnumAlbum): AlbumPageRequest {
        return when (anEnum) {
            EnumAlbum.PURE -> PureMm131Request()
            EnumAlbum.CAMPUS -> CampusMm131Request()
            EnumAlbum.CAR_MM -> CarMm131Request()
            EnumAlbum.QI_PAO -> QipaoMm131Request()
            EnumAlbum.CLASSIC -> ClassicTupianzjRequest()
            EnumAlbum.ART -> ArtTupianzjRequest()
            else -> SexyMm131Request()
        }
    }

    private fun coverBells(bAlbum: BAlbum): Belles {
        return Belles(
            0,
            bAlbum.title,
            bAlbum.href,
            bAlbum.list[0].url ?: bAlbum.cover,
            "",
            bAlbum.tab,
            0,
            GSON.toJson(bAlbum.list),
            System.currentTimeMillis(),
            bAlbum.list[0].referer ?: bAlbum.href
        )
    }

    override fun markFavorite(belles: Belles) {
        belles.favorite = when (belles.favorite) {
            0 -> 1
            else -> 0
        }
        Timber.d("markFavorite@ ${belles.title}")
        Thread { localDB.updateBelles(belles) }.start()
    }

    override fun queryAllFavoriteBelles(): List<Belles>? {
        return localDB.queryAllFavoriteBelles()
    }

    override fun albumPage(anEnum: EnumAlbum): Int {
        return findAlbumRequest(anEnum).page
    }


}