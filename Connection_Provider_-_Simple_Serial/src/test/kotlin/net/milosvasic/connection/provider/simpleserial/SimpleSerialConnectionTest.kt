package net.milosvasic.connection.provider.simpleserial

import net.milosvasic.connection.provider.commons.ConnectionCallback
import net.milosvasic.connection.provider.commons.ConnectionProvider
import net.milosvasic.logger.ConsoleLogger
import net.milosvasic.testing.toolkit.ToolkitTest
import org.junit.Assert
import java.io.File

class SimpleSerialConnectionTest : ToolkitTest() {

    private var toWrite = ""
    private var expectedError = ""
    private val logger = ConsoleLogger()
    private val tag = javaClass.simpleName
    private val path = "${System.getProperty("user.home")}/test.txt"

    private val callback = object : ConnectionCallback {
        override fun onConnectivityChanged(connected: Boolean) {
            log("Connected: $connected")
            unlock()
        }

        override fun onDataReceived(data: ByteArray) {
            val received = String(data)
            log("Received: $received")
            unlock()
        }

        override fun onDataWritten(data: ByteArray) {
            val written = String(data)
            log("Written: $written (to write was: $toWrite)")
            Assert.assertTrue(written == toWrite)
            unlock()
        }

        override fun onError(error: String) {
            if (error == expectedError) {
                log("Received expected error: $error}")
                expectedError = ""
                unlock()
                return
            }
            fail(error)
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
        val start = System.currentTimeMillis()
        val criteria = SimpleSerialConnectionProvidingCriteria(path, callback)
        val connection = ConnectionProvider.provide(criteria)
        Assert.assertNotNull(connection)
        // Connecting - Disconnecting N times in a row
        for (x in 0..10) {
            // Connect
            connection.connect()
            lock()
            // Confirm we are connected.
            Assert.assertTrue(connection.isConnected())
            // Confirm we can't connect twice.
            expectedError = "Already connected"
            connection.connect()
            lock()
            // If assertion below passes that means we received 'Already connected' expected error.
            Assert.assertEquals("", expectedError)
            // Writing data N times in a row
            for (y in 0..10) {
                toWrite = "$y"
                connection.write(toWrite.toByteArray())
                lock()
            }
            // Disconnect
            connection.disconnect()
            lock()
            Assert.assertFalse(connection.isConnected())
        }
        val lasting = System.currentTimeMillis() - start
        log("Test completed in: $lasting")
        Assert.assertTrue(lasting < 500)
    }

    override fun afterTest() {

    }

    private fun fail(error: String) {
        Assert.fail(error)
    }

    private fun log(msg: String) = logger.v(tag, msg)

    private fun err(msg: String) = logger.e(tag, msg)

}