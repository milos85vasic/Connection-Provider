package net.milosvasic.connection.provider.commons

abstract class DataConnection(
        val dataReceiveCallback: DataReceiveCallback,
        override val connectionErrorCallback: ConnectionErrorCallback
) : Connection()