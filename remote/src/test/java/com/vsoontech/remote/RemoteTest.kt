package com.vsoontech.remote

import junit.framework.TestCase

/**
 * Des:
 *
 * @author Weihl
 * Created 2020/12/16
 */
class RemoteTest : TestCase() {


    fun testRemote() {
        val channel = object : RemoteChannel() {
            override val api: String = "api"
            override var respClass: Any = Resp()

            override fun onMessage(resp: Any) {
                println(resp.toString())
            }

        }
        Remote.addChannel(channel)

        Remote.dispatchMessage("{\"api\":\"api\",\"name\":\"alskdjflk\",\"title\":\"123123\"}")
    }

    class Req(
        val name: String
    )


}