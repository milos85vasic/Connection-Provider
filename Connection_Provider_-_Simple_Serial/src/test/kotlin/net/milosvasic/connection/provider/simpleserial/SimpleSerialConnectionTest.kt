package net.milosvasic.connection.provider.simpleserial

import net.milosvasic.connection.provider.commons.ConnectionCallback
import net.milosvasic.connection.provider.commons.ConnectionProvider
import net.milosvasic.testing.toolkit.ToolkitTest
import org.junit.Assert
import java.io.File

class SimpleSerialConnectionTest : ToolkitTest() {

    private var toWrite = ""
    private val path = "${System.getProperty("user.home")}/test.txt"

    private val callback = object : ConnectionCallback {
        override fun onConnectivityChanged(connected: Boolean) {
            unlock()
        }

        override fun onDataReceived(data: ByteArray) {
            // TODO: Implement this.
        }

        override fun onDataWritten(data: ByteArray) {
            Assert.assertTrue(data.toString() == toWrite)
            unlock()
        }

        override fun onError(error: String) {
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
        val criteria = SimpleSerialConnectionProvidingCriteria(path, callback)
        val connection = ConnectionProvider.provide(criteria)
        Assert.assertNotNull(connection)
        // Connecting - Disconnecting in a row.
        for (x in 0..10) {
            connection.connect()
            lock()

            // Confirm we are connected.
            Assert.assertTrue(connection.isConnected())

            // Confirm we can't connect twice.
            // connection.connect() // TODO: Will fail!

            // Try to write data N times:
            for (y in 0..10) {
                connection.write("$y\n".toByteArray())
                lock()
            }

            connection.disconnect()
            lock()

            Assert.assertFalse(connection.isConnected())
        }
    }

    override fun afterTest() {

    }

    private fun fail(error: String) {
        Assert.fail(error)
    }

}