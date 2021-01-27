package dev.weihl.belles.data

import dev.weihl.belles.data.local.entity.Belles
import dev.weihl.belles.data.remote.req.AlbumTab

interface DataSource {

    interface Local {

        fun insertBelles(belles: Belles)

        fun updateBelles(belles: Belles)

        fun queryAllFavoriteBelles(): List<Belles>?
        fun queryBelles(href: String): Belles?
    }

    interface Remote {
        fun loadSexyAlbumList(page: Int): List<BAlbum>

        fun syncSexyAlbumDetails(bAlbum: BAlbum)
    }

    interface Repository {

        fun loadAlbumList(tab: AlbumTab, page: Int): List<Belles>

        fun markFavorites(belles: Belles)

        fun queryAllFavoriteBelles(): List<Belles>?
    }

}