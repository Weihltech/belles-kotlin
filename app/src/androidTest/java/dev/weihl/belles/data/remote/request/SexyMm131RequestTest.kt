package dev.weihl.belles.data.remote.request

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import dev.weihl.belles.data.remote.PureMm131Request
import junit.framework.TestCase
import org.junit.Before

class SexyMm131RequestTest : TestCase() {


    private lateinit var context: Context

    @Before
    override fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    fun testSexyMm131Request() {

        val request = PureMm131Request()
        assert(request.loadAlbumList().isNotEmpty())

        val albumList = request.loadAlbumList()

        albumList.forEach {
            request.syncAlbumDetails(it)
            println("SexyMm131@ $it")
        }

    }

}