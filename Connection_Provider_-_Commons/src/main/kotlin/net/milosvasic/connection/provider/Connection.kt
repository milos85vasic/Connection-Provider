package net.milosvasic.connection.provider

interface Connection {

    fun connect()

    fun disconnect()

    fun write(data: ByteArray)

}
