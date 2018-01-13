package net.milosvasic.connection.provider.commons

import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

public object ConnectionProvider : ConnectionProviding<ConnectionProvidingCriteria> {

    private val providers = ConcurrentHashMap<KClass<*>, ConnectionProviding<ConnectionProvidingCriteria>>()

    internal fun registerProvider(
            provider: ConnectionProviding<ConnectionProvidingCriteria>,
            criteria: KClass<*>
    ) {
        providers[criteria] = provider
    }

    override fun provide(criteria: ConnectionProvidingCriteria): Connection {
        val connection = providers[criteria::class]?.provide(criteria)
        if (connection != null) return connection
        throw IllegalArgumentException("Unsupported criteria: $criteria")
    }

}