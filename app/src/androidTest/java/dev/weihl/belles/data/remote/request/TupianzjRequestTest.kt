package dev.weihl.belles.data.remote.request

import dev.weihl.belles.data.remote.req.ClassicTupianzjRequest
import junit.framework.TestCase

class TupianzjRequestTest : TestCase() {

    public override fun setUp() {
        super.setUp()
    }

    fun testTupianzjRequest() {

        val request = ClassicTupianzjRequest()
        val albumList = request.loadAlbumList()
        assert(albumList.isNotEmpty())

        albumList.forEach {
            Thread.sleep(2000)
            request.syncAlbumDetails(it)
            println("TupianzjRequest@ $it")
        }

    }
}