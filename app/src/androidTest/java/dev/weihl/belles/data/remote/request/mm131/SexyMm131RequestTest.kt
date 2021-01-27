package dev.weihl.belles.data.remote.request.mm131

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import junit.framework.TestCase
import org.junit.Before

class SexyMm131RequestTest : TestCase() {


    private lateinit var context: Context

    @Before
    override fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    fun testSexyMm131Request() {

        val request = PureMm131Request(0)
        assert(request.loadAlbumList().isNotEmpty())

        val albumList = request.loadAlbumList()

        request.syncAlbumDetails(albumList[0])
        println("SexyMm131@ " + albumList[0].toString())

    }

}