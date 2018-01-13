package net.milosvasic.connection.provider.simpleserial

import net.milosvasic.connection.provider.commons.ConnectionProvider
import net.milosvasic.connection.provider.commons.DataCallback
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