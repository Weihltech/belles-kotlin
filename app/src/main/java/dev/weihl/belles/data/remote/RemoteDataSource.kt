package dev.weihl.belles.data.remote

import android.content.Context
import dev.weihl.belles.data.BAlbum
import dev.weihl.belles.data.DataSource

class RemoteDataSource(application: Context) : DataSource.Remote {
    override fun loadSexyAlbumList(page: Int): List<BAlbum> {
        TODO("Not yet implemented")
    }

    override fun syncSexyAlbumDetails(bAlbum: BAlbum) {
        TODO("Not yet implemented")
    }

}