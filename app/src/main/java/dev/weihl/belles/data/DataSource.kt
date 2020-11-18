package dev.weihl.belles.data

import dev.weihl.belles.data.local.entity.Belles

interface DataSource {

    interface Local {

        fun hasPageDetail(sexyPage: SexyPage)

        fun queryBelles(sexyPage: SexyPage): Belles?

        fun queryAllBelles(): ArrayList<Belles>?

        fun insertBelles(belles: Belles)

        fun updateBelles(belles: Belles)

        fun queryAllFavoriteBelles(): ArrayList<Belles>
    }

    interface Remote {
        fun loadSexyPageList(page: Int): ArrayList<SexyPage>

        fun loadSexyImageList(sexyPage: SexyPage): ArrayList<SexyImage>
    }

}