package net.milosvasic.connection.provider.commons

interface ConnectionProviding<in T : ConnectionProvidingCriteria> {

    fun provide(criteria: T): Connection

}