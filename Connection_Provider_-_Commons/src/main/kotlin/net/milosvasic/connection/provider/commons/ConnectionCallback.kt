package net.milosvasic.connection.provider.commons

interface ConnectionCallback {

    fun onError(error: String)

    fun onDataReceived(data: ByteArray)

    fun onDataWritten(data: ByteArray)

    fun onConnectivityChanged(connected: Boolean)

}