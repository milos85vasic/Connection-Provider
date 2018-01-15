package net.milosvasic.connection.provider.simpleserial

import net.milosvasic.connection.provider.commons.ConnectionCallback
import net.milosvasic.connection.provider.commons.ConnectionProvider
import net.milosvasic.testing.toolkit.ToolkitTest
import org.junit.Assert
import java.io.File

class SimpleSerialConnectionTest : ToolkitTest() {

    private val path = "${System.getProperty("user.home")}/test.txt"

    private val callback = object : ConnectionCallback {
        override fun onData(data: ByteArray) {
            Assert.fail("! ! ! ! ! ")
        }

        override fun onError(e: Exception) {
            fail(e)
        }

        override fun onConnectivityChanged(connected: Boolean) {
            // TODO("not implemented")
        }
    }

    override fun beforeTest() {
        val testFile = File(path)
        if (testFile.exists()) {
            testFile.delete()
        }
        Assert.assertTrue(testFile.createNewFile())
        Assert.assertTrue(testFile.exists())
    }

    override fun testImplementation() {
        val criteria = SimpleSerialConnectionProvidingCriteria(path, callback)
        val connection = ConnectionProvider.provide(criteria)
        Assert.assertNotNull(connection)
        // Connecting - Disconnecting in a row.
        // for (x in 0..10) {       // TODO: Return back 10 iterations.
        connection.connect()
        sleep(1)    // TODO: Async with callbacks.

        // Confirm we are connected.
        Assert.assertTrue(connection.isConnected())

        // Confirm we can't connect twice.
//        var failed = false
//        try {
//            connection.connect()
//        } catch (e: Exception) {
//            failed = true
//        }
//        Assert.assertTrue(failed)

        // Try to write data:
        try {
            connection.write("Test\n".toByteArray())
        } catch (e: Exception) {
            fail(e)
        }
        sleep(1)       // TODO: Async with callbacks.

        connection.disconnect()
        sleep(1)       // TODO: Async with callbacks.

        Assert.assertFalse(connection.isConnected())
        // }
    }

    override fun afterTest() {

    }

    private fun fail(e: Exception) {
        Assert.fail("Error: $e")
    }

}