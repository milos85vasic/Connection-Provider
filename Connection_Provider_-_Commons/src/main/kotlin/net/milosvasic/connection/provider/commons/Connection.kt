package net.milosvasic.connection.provider.commons

abstract class Connection(val callback: ConnectionCallback) {

    protected abstract val executor: Executor

    abstract fun connect()

    abstract fun disconnect()

    abstract fun write(data: ByteArray)

    abstract fun isConnected(): Boolean

}
