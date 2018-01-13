package net.milosvasic.connection.provider.simpleserial

import net.milosvasic.connection.provider.commons.ConnectionErrorCallback
import net.milosvasic.connection.provider.commons.ConnectionProvider
import net.milosvasic.connection.provider.commons.DataReceiveCallback
import net.milosvasic.logger.ConsoleLogger
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.File

class SimpleSerialConnectionTest {

    val logger = ConsoleLogger()
    val tag = "Simple serial connection test"
    val path = "${System.getProperty("user.home")}/test.txt"

    private val dataCallback = object : DataReceiveCallback {
        override fun onData(data: ByteArray) {
            Assert.fail("! ! ! ! ! ")
        }
    }

    private val errorCallback = object : ConnectionErrorCallback {
        override fun onError(e: Exception) {
            fail(e)
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
        val criteria = SimpleSerialConnectionProvidingCriteria(path, dataCallback, errorCallback)
        val connection = ConnectionProvider.provide(criteria)
        Assert.assertNotNull(connection)
        // Connecting - Disconnecting in a row.
        // for (x in 0..10) {
        connection.connect()

        // Confirm we can't connect twice.
//        var failed = false
//        try {
//            connection.connect()
//        } catch (e: Exception) {
//            failed = true
//        }
//        Assert.assertTrue(failed)
        // Try to write data:

//        try {
//            connection.write("Test\n".toByteArray())
//        } catch (e: Exception) {
//            fail(e)
//        }

        Thread.sleep(2000)

        // Confirm we are connected.
        Assert.assertTrue(connection.isConnected())

        connection.disconnect()

        Assert.assertFalse(connection.isConnected())
        // }
    }

    private fun fail(e: Exception) {
        Assert.fail("Error: $e")
    }

}