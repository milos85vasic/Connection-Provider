package net.milosvasic.connection.provider

abstract class ConnectionProvidingCriteria {

    init {
        registerProvider()
    }

    private fun registerProvider() = ConnectionProvider.registerProvider(getProvider(), this::class)

    internal abstract fun getProvider(): ConnectionProviding<ConnectionProvidingCriteria>

}