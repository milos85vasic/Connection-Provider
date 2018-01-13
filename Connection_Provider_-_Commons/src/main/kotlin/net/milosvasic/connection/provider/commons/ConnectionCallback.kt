package net.milosvasic.connection.provider.commons

interface ConnectionCallback {

    fun onError(e: Exception)

    fun onData(data: ByteArray)

    fun onConnectivityChanged(connected: Boolean)

}