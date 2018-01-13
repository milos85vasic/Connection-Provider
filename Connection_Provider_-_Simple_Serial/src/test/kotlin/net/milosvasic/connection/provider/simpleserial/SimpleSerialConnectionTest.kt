package net.milosvasic.connection.provider.simpleserial

import net.milosvasic.connection.provider.commons.ConnectionCallback
import net.milosvasic.connection.provider.commons.ConnectionProvider
import net.milosvasic.logger.ConsoleLogger
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.File

class SimpleSerialConnectionTest {

    val logger = ConsoleLogger()
    val tag = "Simple serial connection test"
    val path = "${System.getProperty("user.home")}/test.txt"

    private val callback = object : ConnectionCallback {
        override fun onData(data: ByteArray) {
            Assert.fail("! ! ! ! ! ")
        }

        override fun onError(e: Exception) {
            fail(e)
        }

        override fun onConnectivityChanged(connected: Boolean) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    @Before
    fun beforeTest(){
        val testFile = File(path)
        if (testFile.exists()) {
            testFile.delete()
        }
        Assert.assertTrue(testFile.createNewFile())
        Assert.assertTrue(testFile.exists())
    }

    @Test
    fun testSimpleSerialConnection() {
        val criteria = SimpleSerialConnectionProvidingCriteria(path, callback)
        val connection = ConnectionProvider.provide(criteria)
        Assert.assertNotNull(connection)
        // Connecting - Disconnecting in a row.
        // for (x in 0..10) {       // TODO: Return back 10 iterations.
        connection.connect()
        Thread.sleep(1000)    // TODO: Async with callbacks.

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
        Thread.sleep(1000)       // TODO: Async with callbacks.

        connection.disconnect()
        Thread.sleep(1000)       // TODO: Async with callbacks.

        Assert.assertFalse(connection.isConnected())
        // }
    }

    private fun fail(e: Exception) {
        Assert.fail("Error: $e")
    }

}