package net.milosvasic.connection.provider.commons

interface Connection {

    fun connect()

    fun disconnect()

    fun write(data: ByteArray)

}
