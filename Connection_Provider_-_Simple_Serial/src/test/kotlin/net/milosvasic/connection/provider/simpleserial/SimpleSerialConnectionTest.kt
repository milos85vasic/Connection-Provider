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
            val twrt = toWrite
            toWrite = ""
            val written = String(data)
            log("Written: $written (to write was: $twrt)")
            Assert.assertTrue(written == twrt)
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
        // Repeat test N times
        for (z in 0..10) {
            val start = System.currentTimeMillis()
            val criteria = SimpleSerialConnectionProvidingCriteria(path, callback)
            val connection = ConnectionProvider.provide(criteria)
            Assert.assertNotNull(connection)
            // Connecting - Disconnecting N times in a row
            for (x in 0..10) {
                // Ensure not connected
                Assert.assertFalse(connection.isConnected())
                // Connect
                connection.connect()
                if (connection.isConnected()) {
                    wrn("Fast connected.")
                } else {
                    lock()
                }
                // Confirm we are connected.
                Assert.assertTrue(connection.isConnected())
                // Confirm we can't connect twice.
                expectedError = "Already connected"
                connection.connect()
                // If expected error is empty, then we executed this ver fast.
                // In that case we do not need any lock.
                if (expectedError == "") {
                    wrn("Fast expected exception.")
                } else {
                    lock()
                }
                // If assertion below passes that means we received 'Already connected' expected error.
                Assert.assertEquals("", expectedError)
                // Writing data N times in a row
                for (y in 0..10) {
                    toWrite = "$y"
                    log("To write: $y")
                    connection.write(toWrite.toByteArray())
                    if (!toWrite.isEmpty()) {
                        lock()
                    } else {
                        wrn("Fast written.")
                    }
                }
                // Disconnect
                // Ensure connected
                Assert.assertTrue(connection.isConnected())
                connection.disconnect()
                if (connection.isConnected()) {
                    lock()
                } else {
                    wrn("Fast disconnected.")
                }
                Assert.assertFalse(connection.isConnected())
            }
            val lasting = System.currentTimeMillis() - start
            log("Test completed in: $lasting")
            Assert.assertTrue(lasting < 500)
            sleep(500, false)
        }
    }

    override fun afterTest() {

    }

    private fun fail(error: String) {
        Assert.fail(error)
    }

    private fun log(msg: String) = logger.v(tag, msg)

    private fun err(msg: String) = logger.e(tag, msg)

    private fun wrn(msg: String) = logger.w(tag, msg)

}