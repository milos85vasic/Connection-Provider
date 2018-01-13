package net.milosvasic.connection.provider.simpleserial

import net.milosvasic.connection.provider.commons.ConnectionErrorCallback
import net.milosvasic.connection.provider.commons.DataReceiveCallback
import net.milosvasic.connection.provider.commons.DataConnection
import net.milosvasic.connection.provider.commons.Executor
import java.io.*
import java.nio.charset.StandardCharsets

class SimpleSerialConnection internal constructor(
        dataReceiveCallback: DataReceiveCallback,
        connectionErrorCallback: ConnectionErrorCallback,
        private val comPortOut: String,
        private val comPortIn: String?
) : DataConnection(dataReceiveCallback, connectionErrorCallback) {

    override val executor: Executor
        get() = Executor.obtainExecutor(2)

    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null

    override fun connect() {
        if (isConnected()) {
            connectionErrorCallback.onError(IllegalStateException("Already connected"))
            return
        }
        val fileOut = File(comPortOut)
        try {
            outputStream = BufferedOutputStream(FileOutputStream(fileOut))
        } catch (e: Exception) {
            connectionErrorCallback.onError(e)
            return
        }
        var inPath = comPortOut
        comPortIn?.let {
            inPath = comPortIn
        }
        val fileIn = File(inPath)
        try {
            inputStream = BufferedInputStream(FileInputStream(fileIn))
            startReading()
        } catch (e: Exception) {
            connectionErrorCallback.onError(e)
        }
    }

    override fun disconnect() {
        inputStream?.close()
        outputStream?.close()
        inputStream = null
        outputStream = null
    }

    override fun write(data: ByteArray) {
        executor.execute {
            outputStream?.let {
                outputStream?.write(data)
                return@execute
            }
            connectionErrorCallback.onError(IllegalStateException("Not connected."))
        }
    }

    override fun isConnected() = inputStream != null && outputStream != null

    private fun startReading() {
        executor.execute {
            val bufferedReader = BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8))
            while (inputStream != null) {
                try {
                    val line = bufferedReader.readLine()
                    line?.let {
                        dataReceiveCallback.onData(line.toByteArray())
                    }
                } catch (e: Exception) {
                    disconnect()
                    connectionErrorCallback.onError(e)
                }
            }
        }
    }

}