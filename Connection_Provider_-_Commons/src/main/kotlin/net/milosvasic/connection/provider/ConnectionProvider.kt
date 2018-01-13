package net.milosvasic.connection.provider

import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

object ConnectionProvider : ConnectionProviding<ConnectionProvidingCriteria> {

    private val providers = ConcurrentHashMap<KClass<ConnectionProvidingCriteria>, ConnectionProviding<ConnectionProvidingCriteria>>()

    internal fun registerProvider(
            provider: ConnectionProviding<ConnectionProvidingCriteria>,
            criteria: KClass<ConnectionProvidingCriteria>
    ) {
        providers[criteria] = provider
    }

    internal fun unregisterProvider(criteria: KClass<ConnectionProvidingCriteria>) {
        providers.remove(criteria)
    }

    override fun provide(criteria: ConnectionProvidingCriteria): Connection {
        val connection = providers[criteria::class]?.provide(criteria)
        connection?.let {
            return@let
        }
        throw IllegalArgumentException("Unsupported criteria: $criteria")
    }

}