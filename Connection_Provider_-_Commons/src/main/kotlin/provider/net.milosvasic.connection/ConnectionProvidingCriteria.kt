package provider.net.milosvasic.connection

interface ConnectionProvidingCriteria {

    fun provide(): Connection

}