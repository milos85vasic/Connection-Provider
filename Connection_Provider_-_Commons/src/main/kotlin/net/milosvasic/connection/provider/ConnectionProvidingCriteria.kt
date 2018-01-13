package net.milosvasic.connection.provider

public abstract class ConnectionProvidingCriteria {

    private fun registerProvider() = ConnectionProvider.registerProvider(getProvider(), this::class)

    protected abstract fun getProvider(): ConnectionProviding<ConnectionProvidingCriteria>

    init {
        registerProvider()
    }

}