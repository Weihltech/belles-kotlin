package dev.weihl.belles.data

import dev.weihl.belles.data.local.entity.Belles

interface DataSource {

    interface Local {

        fun hasPageDetail(sexyPage: SexyPage)

        fun queryBelles(sexyPage: SexyPage): Belles?

        fun insertBelles(belles: Belles)
    }

    interface Remote {
        fun loadSexyPageList(page: Int): ArrayList<SexyPage>

        fun loadSexyImageList(sexyPage: SexyPage): ArrayList<SexyImage>
    }

}