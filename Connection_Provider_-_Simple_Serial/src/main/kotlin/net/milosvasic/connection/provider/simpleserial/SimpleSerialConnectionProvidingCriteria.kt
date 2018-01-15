package net.milosvasic.connection.provider.simpleserial

import net.milosvasic.connection.provider.commons.*

class SimpleSerialConnectionProvidingCriteria(
        val comPort: String,
        val callback: ConnectionCallback,
        val comPortOut: String? = null
) : ConnectionProvidingCriteria() {

    override fun getProvider() = object : ConnectionProviding<ConnectionProvidingCriteria> {
        override fun provide(criteria: ConnectionProvidingCriteria) = SimpleSerialConnection(
                callback,
                comPort,
                comPortOut
        )
    }

}