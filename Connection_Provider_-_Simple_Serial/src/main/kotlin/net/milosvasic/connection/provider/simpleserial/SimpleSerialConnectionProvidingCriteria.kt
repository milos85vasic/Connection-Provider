package net.milosvasic.connection.provider.simpleserial

import net.milosvasic.connection.provider.ConnectionProviding
import net.milosvasic.connection.provider.ConnectionProvidingCriteria
import net.milosvasic.connection.provider.DataCallback

public class SimpleSerialConnectionProvidingCriteria(
        val comPort: String,
        val callback: DataCallback,
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