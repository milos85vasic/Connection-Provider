package net.milosvasic.connection.provider.simpleserial

import net.milosvasic.connection.provider.commons.ConnectionErrorCallback
import net.milosvasic.connection.provider.commons.DataReceiveCallback
import net.milosvasic.connection.provider.commons.DataConnection
import net.milosvasic.connection.provider.commons.Executor
import java.io.*
import java.nio.charset.StandardCharsets

public class SimpleSerialConnection internal constructor(
        dataReceiveCallback: DataReceiveCallback,
        connectionErrorCallback: ConnectionErrorCallback,
        internal val comPort: String,
        internal val comPortOut: String?
) : DataConnection(dataReceiveCallback, connectionErrorCallback) {

    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null

    override fun connect() {
        if (inputStream != null || outputStream != null) {
            throw IllegalStateException("Already connected")
        }
        val fileIn = File(comPort)
        if (fileIn.exists()) {
            inputStream = BufferedInputStream(FileInputStream(fileIn))
            val bufferedReader = BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8))
            Executor.execute(Runnable {
                while (inputStream != null) {
                    try {
                        val line = bufferedReader.readLine()
                        if (line != null) {
                            dataReceiveCallback.onData(line.toByteArray())
                        }
                    } catch (e: Exception) {
                        connectionErrorCallback.onError(e)
                    }
                }
            })
        }
        comPortOut?.let {
            val fileOut = File(comPortOut)
            if (fileOut.exists()) {
                outputStream = BufferedOutputStream(FileOutputStream(fileOut))
            }
            return
        }
        val fileOut = File(comPort)
        if (fileOut.exists()) {
            outputStream = BufferedOutputStream(FileOutputStream(fileOut))
            return
        }
        throw IllegalArgumentException("Unable to initialize input connection to: $comPort")
    }

    override fun disconnect() {
        inputStream?.close()
        outputStream?.close()
        inputStream = null
        outputStream = null
    }

    override fun write(data: ByteArray) {
        outputStream?.let {
            outputStream?.write(data)
            return
        }
        throw IllegalStateException("Not connected.")
    }

}