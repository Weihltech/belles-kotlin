package dev.weihl.belles.data.remote.request

import junit.framework.TestCase

class TupianzjRequestTest : TestCase() {

    public override fun setUp() {
        super.setUp()
    }

    fun testTupianzjRequest() {

        val request = ArtTupianzjRequest(0)
        val albumList = request.loadAlbumList()
        assert(albumList.isNotEmpty())

        request.syncAlbumDetails(albumList[0])
        println("Tupianzj@ " + albumList[0])

    }
}