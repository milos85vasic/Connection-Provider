package net.milosvasic.connection.provider.simpleserial

import net.milosvasic.connection.provider.commons.ConnectionErrorCallback
import net.milosvasic.connection.provider.commons.ConnectionProviding
import net.milosvasic.connection.provider.commons.ConnectionProvidingCriteria
import net.milosvasic.connection.provider.commons.DataReceiveCallback

class SimpleSerialConnectionProvidingCriteria(
        val comPort: String,
        val dataReceiveCallback: DataReceiveCallback,
        val connectionErrorCallback: ConnectionErrorCallback,
        val comPortOut: String? = null
) : ConnectionProvidingCriteria() {

    override fun getProvider() = object : ConnectionProviding<ConnectionProvidingCriteria> {
        override fun provide(criteria: ConnectionProvidingCriteria) = SimpleSerialConnection(
                dataReceiveCallback,
                connectionErrorCallback,
                comPort,
                comPortOut
        )
    }

}