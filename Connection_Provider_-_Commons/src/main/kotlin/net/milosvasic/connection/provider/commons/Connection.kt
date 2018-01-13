package net.milosvasic.connection.provider.commons

abstract class Connection(connectionErrorCallback: ConnectionErrorCallback) {

    abstract fun connect()

    abstract fun disconnect()

    abstract fun write(data: ByteArray)

}
