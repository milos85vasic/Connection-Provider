package net.milosvasic.connection.provider

interface ConnectionProviding {

    fun provide(criteria: ConnectionProvidingCriteria): Connection

}