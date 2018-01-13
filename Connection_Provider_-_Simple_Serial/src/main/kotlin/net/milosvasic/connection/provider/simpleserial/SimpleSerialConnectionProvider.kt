package net.milosvasic.connection.provider.simpleserial

import net.milosvasic.connection.provider.Connection
import net.milosvasic.connection.provider.ConnectionProviding

public class SimpleSerialConnectionProvider : ConnectionProviding<SimpleSerialConnectionProvidingCriteria> {

    override fun provide(criteria: SimpleSerialConnectionProvidingCriteria): Connection {
        return SimpleSerialConnection(
                criteria.callback,
                criteria.comPort,
                criteria.comPortOut
        )
    }

}