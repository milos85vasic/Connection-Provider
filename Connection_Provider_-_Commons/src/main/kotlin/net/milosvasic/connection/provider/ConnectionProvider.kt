package net.milosvasic.connection.provider

import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

object ConnectionProvider : ConnectionProviding {

    private val providers = ConcurrentHashMap<KClass<ConnectionProvidingCriteria>, ConnectionProviding>()

    fun registerProvider(
            provider: ConnectionProviding,
            criteria: KClass<ConnectionProvidingCriteria>
    ) {
        providers[criteria] = provider
    }

    fun unregisterProvider(criteria: KClass<ConnectionProvidingCriteria>) {
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