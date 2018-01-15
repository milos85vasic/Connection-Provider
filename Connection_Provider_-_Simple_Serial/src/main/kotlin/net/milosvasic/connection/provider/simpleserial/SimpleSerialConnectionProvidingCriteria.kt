package net.milosvasic.connection.provider.simpleserial

import net.milosvasic.connection.provider.commons.*

class SimpleSerialConnectionProvidingCriteria(
        val comPortOut: String,
        val callback: ConnectionCallback,
        val comPortIn: String? = null
) : ConnectionProvidingCriteria() {

    override fun getProvider() = object : ConnectionProviding<ConnectionProvidingCriteria> {
        override fun provide(criteria: ConnectionProvidingCriteria) = SimpleSerialConnection(
                callback,
                comPortOut,
                comPortIn
        )
    }

}