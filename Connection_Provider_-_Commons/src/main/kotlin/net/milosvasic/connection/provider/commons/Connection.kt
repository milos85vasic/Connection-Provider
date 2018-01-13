package net.milosvasic.connection.provider.commons

abstract class Connection {

    protected abstract val executor: Executor

    abstract val connectionErrorCallback: ConnectionErrorCallback

    abstract fun connect()

    abstract fun disconnect()

    abstract fun write(data: ByteArray)

    abstract fun isConnected(): Boolean

}
