package net.milosvasic.connection.provider.commons

public abstract class DataConnection(
        val dataReceiveCallback: DataReceiveCallback,
        val connectionErrorCallback: ConnectionErrorCallback
) : Connection(connectionErrorCallback)