package net.milosvasic.connection.provider.simpleserial

import net.milosvasic.connection.provider.commons.ConnectionErrorCallback
import net.milosvasic.connection.provider.commons.ConnectionProvider
import net.milosvasic.connection.provider.commons.DataReceiveCallback
import net.milosvasic.logger.ConsoleLogger
import org.junit.Assert
import org.junit.Test

public class SimpleSerialConnectionTest {

    val logger = ConsoleLogger()
    val tag = "Simple serial connection test"

    private val dataCallback = object : DataReceiveCallback {
        override fun onData(data: ByteArray) {
            logger.d(tag, data.toString())
        }
    }

    private val errorCallback = object : ConnectionErrorCallback {
        override fun onError(e: Exception) {
            fail(e)
        }
    }

    @Test
    fun testSimpleSerialConnection() {
        val criteria = SimpleSerialConnectionProvidingCriteria(
                "/dev/stdin", dataCallback, errorCallback, "/dev/stdout"
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
            // Try to write data:
            try {
                connection.write("Test\n".toByteArray())
            } catch (e: Exception) {
                fail(e)
            }
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