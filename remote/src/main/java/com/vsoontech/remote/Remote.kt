package com.vsoontech.remote

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import java.lang.Exception
import java.net.URI
import java.util.*

/**
 * Des:
 * 远程操作
 *
 * @author Weihl
 * Created 2020/12/16
 */
internal class Remote private constructor() {
    companion object {
        private val mGson = Gson()
        private val channelMap = WeakHashMap<String, RemoteChannel>()
        private var mClient: RemoteClient? = null
        var uri: String = "ws://localhost:8887"
        private val client: RemoteClient
            get() {
                if (mClient == null) {
                    synchronized(Remote::class.java) {
                        if (mClient == null) {
                            mClient = RemoteClient(URI(uri))
                        }
                    }
                }
                return mClient!!
            }
        val isOpen: Boolean
            get() {
                return client.isOpen
            }

        private fun close() {
            client.close()
            mClient = null
        }

        fun connect() {
            try {
                client.connect()
            } catch (ex: Exception) {
                // connecting
            }
        }

        fun addChannel(channel: RemoteChannel) {
            channelMap[channel.unique] = channel
        }

        fun removeChannel(channel: RemoteChannel) {
            channelMap.remove(channel.unique)
            if (channelMap.size == 0) {
                close()
            }
        }

        fun send(content: String) {
            client.send(content)
        }

        fun dispatchMessage(message: String) {
            for (channel in channelMap.values) {

                channel.onMessage(mGson.fromJson(message, channel.respClass::class.java))
            }
        }

        fun dispatchOpen() {
            for (channel in channelMap.values) {
                channel.onOpen()
            }
        }

    }


}