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
        val criteria = SimpleSerialConnectionProvidingCriteria(
                "/dev/stdin",
                callback,
                "/dev/stdout"
        )
        val connection = ConnectionProvider.provide(criteria)
        Assert.assertNotNull(connection)
        // Connecting - Disconnecting in a row.
        for (x in 0..10) {
            try {
                connection.connect()
            } catch (e: Exception) {
                fail(e)
            }
            // Confirm we can't connect twice.
            var failed = false
            try {
                connection.connect()
            } catch (e: Exception) {
                failed = true
            }
            Assert.assertTrue(failed)
            try {
                connection.disconnect()
            } catch (e: Exception) {
                fail(e)
            }
        }
    }

    private fun fail(e: Exception) {
        Assert.fail("Error: $e")
    }

}