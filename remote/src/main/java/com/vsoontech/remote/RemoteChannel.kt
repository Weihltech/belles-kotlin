package com.vsoontech.remote

/**
 * Des:
 * 远程连接主题
 *
 * @author Weihl
 * Created 2020/12/16
 */
abstract class RemoteChannel {

    abstract val api: String
    abstract var respClass: Any

    internal val unique: String
        get() {
            return "$api-${System.currentTimeMillis()}"
        }

    open fun onOpen() {

    }

    open fun onClose() {

    }

    abstract fun onMessage(resp: Any)

    open fun onError(ex: Exception?) {

    }

    open fun send(content: String) {
        Remote.send(content)
    }

    fun disconnect() {
        Remote.removeChannel(this)
    }

    fun connect() {

        // add channel
        Remote.addChannel(this)

        // adjust
        if (Remote.isOpen) {
            onOpen()
            return
        }

        // connect
        Remote.connect()
    }

}