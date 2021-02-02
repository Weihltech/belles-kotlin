package dev.weihl.belles.data

import dev.weihl.belles.data.local.entity.Belles
import dev.weihl.belles.data.remote.EnumAlbum

interface DataSource {

    interface Local {
        fun insertBelles(belles: Belles)

        fun updateBelles(belles: Belles)

        fun queryAllFavoriteBelles(): List<Belles>?

        fun queryBellesByHref(href: String): Belles?
    }

    interface Repository {
        fun nextAlbumList(anEnum: EnumAlbum): List<Belles>

        fun loadAlbumList(anEnum: EnumAlbum, page: Int): List<Belles>

        fun markFavorite(belles: Belles)

        fun queryAllFavoriteBelles(): List<Belles>?
    }

}