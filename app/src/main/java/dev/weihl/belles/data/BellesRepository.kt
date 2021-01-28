package dev.weihl.belles.data

import com.google.gson.Gson
import dev.weihl.belles.MainApp
import dev.weihl.belles.data.local.LocalDataSource
import dev.weihl.belles.data.local.entity.Belles
import dev.weihl.belles.data.remote.req.*
import timber.log.Timber

object BellesRepository : DataSource.Repository {

    private val mContext = MainApp.getContext()
    private var localDB: LocalDataSource = LocalDataSource(mContext)
    private val GSON = Gson()

    // 只有浏览过，才初始化当前项数据 request
    private var sexyRequest: AlbumPageRequest? = null
    private var pureRequest: AlbumPageRequest? = null
    private var campusRequest: AlbumPageRequest? = null
    private var carMmRequest: AlbumPageRequest? = null
    private var qipaoRequest: AlbumPageRequest? = null
    private var classicRequest: AlbumPageRequest? = null
    private var artRequest: AlbumPageRequest? = null

    private val albumRequestMap = HashMap<String, AlbumPageRequest>()

    override fun nextAlbumList(anEnum: EnumAlbum): List<Belles> {
        TODO("Not yet implemented")
    }

    override fun loadAlbumList(anEnum: EnumAlbum, page: Int): List<Belles> {

        val albumRequest = findAlbumRequest(anEnum, page)
        val sexyAlbumList = albumRequest.loadAlbumList()
        // 取网络数据
        val sexyBelles = mutableListOf<Belles>()
        sexyAlbumList.forEach {
            // 若有本地记录，则取本地数据
            val localBelles = localDB.queryBelles(it.href)
            if (localBelles != null) {
                Timber.d(localBelles.toString())
                sexyBelles.add(0, localBelles)
            } else {
                // snyc remote
                albumRequest.syncAlbumDetails(it)
                // cover
                val newBelles = coverBells(it)
                sexyBelles.add(0, newBelles)
                // save album
                localDB.insertBelles(newBelles)
                Timber.d(newBelles.toString())
            }
        }

        return sexyBelles
    }

    private fun findAlbumRequest(anEnum: EnumAlbum, page: Int): AlbumPageRequest {
        var request = albumRequestMap[anEnum.tab]
        if (request == null) {
            request = selectAlbumRequest(anEnum)
            albumRequestMap[anEnum.tab] = request
        }
        request.page = page
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