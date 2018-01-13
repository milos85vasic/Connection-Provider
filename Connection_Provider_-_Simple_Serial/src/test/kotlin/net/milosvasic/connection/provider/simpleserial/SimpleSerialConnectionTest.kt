package net.milosvasic.connection.provider.simpleserial

import net.milosvasic.connection.provider.ConnectionProvider
import net.milosvasic.connection.provider.DataCallback
import org.junit.Assert
import org.junit.Test

public class SimpleSerialConnectionTest {

    private val callback = object : DataCallback {
        override fun onData(data: ByteArray) {

        }
    }

    @Test
    fun testSimpleSerialConnection() {
        val criteria = SimpleSerialConnectionProvidingCriteria("", callback, "")
        val connection = ConnectionProvider.provide(criteria)
        Assert.assertNotNull(connection)
    }

}