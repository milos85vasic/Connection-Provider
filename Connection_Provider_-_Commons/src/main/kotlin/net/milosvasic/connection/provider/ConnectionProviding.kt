package net.milosvasic.connection.provider

interface ConnectionProviding<in T : ConnectionProvidingCriteria> {

    fun provide(criteria: T): Connection

}