package provider.net.milosvasic.connection

interface ConnectionProviding {

    fun provide(criteria: ConnectionProvidingCriteria): Connection

}