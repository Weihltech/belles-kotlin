package com.vsoontech.remote

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI

/**
 * Des:
 * 远程连接客户端
 *
 * @author Weihl
 * Created 2020/12/16
 */
internal class RemoteClient(serverUri: URI) : WebSocketClient(serverUri) {

    override fun onOpen(handshakedata: ServerHandshake?) {
        Remote.dispatchOpen()
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {

    }

    override fun onMessage(message: String?) {
        if (message != null) {
            Remote.dispatchMessage(message)
        }
    }

    override fun onError(ex: Exception?) {

    }

}