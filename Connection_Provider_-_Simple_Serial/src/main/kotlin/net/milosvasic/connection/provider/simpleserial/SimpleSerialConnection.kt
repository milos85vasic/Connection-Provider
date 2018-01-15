package net.milosvasic.connection.provider.simpleserial

import net.milosvasic.connection.provider.commons.*
import java.io.*
import java.util.concurrent.atomic.AtomicBoolean

class SimpleSerialConnection internal constructor(
        callback: ConnectionCallback,
        private val comPortOut: String,
        private val comPortIn: String?
) : DataConnection(callback) {

    private val connected = AtomicBoolean()
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null

    override val executor = Executor.obtainExecutor(2)

    override fun connect() {
        executor.execute {
            if (isConnected()) {
                callback.onError("Already connected")
                return@execute
            }
            val fileOut = File(comPortOut)
            try {
                outputStream = BufferedOutputStream(FileOutputStream(fileOut))
                if (outputStream == null) {
                    disconnect("Couldn't connect to: $comPortOut")
                }
            } catch (e: Exception) {
                disconnect(e.message)
                return@execute
            }
            var inPath = comPortOut
            comPortIn?.let {
                inPath = comPortIn
            }
            val fileIn = File(inPath)
            try {
                inputStream = BufferedInputStream(FileInputStream(fileIn))
                inputStream?.let {
                    startReading()
                    return@execute
                }
                disconnect("Couldn't connect to: $fileIn")
            } catch (e: Exception) {
                disconnect(e.message)
                return@execute
            }
        }
    }

    override fun disconnect() {
        disconnect(null)
    }

    override fun write(data: ByteArray) {
        executor.execute {
            outputStream?.let {
                outputStream?.write(data)
                callback.onDataWritten(data)
                return@execute
            }
            disconnect("Not connected.")
        }
    }

    override fun isConnected() = connected.get()

    private fun startReading() {
        executor.execute {
            //            val bufferedReader: BufferedReader?
//            try {
//                bufferedReader = BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8))
//            } catch (e: Exception) {
//                disconnect(e)
//                return@execute
//            }
//            while (inputStream != null) {
//                try {
//                    val line = bufferedReader.readLine()
//                    line?.let {
//                        dataReceiveCallback.onDataReceived(line.toByteArray())
//                    }
//                } catch (e: Exception) {
//                    disconnect(e)
//                }
//            }
            setConnected(true)
        }
    }

    private fun setConnected(toggle: Boolean) {
        connected.set(toggle)
        callback.onConnectivityChanged(isConnected())
    }

    private fun disconnect(error: String?) {
        executor.execute {
            inputStream?.close()
            outputStream?.close()
            inputStream = null
            outputStream = null
            error?.let {
                callback.onError(error)
            }
            setConnected(false)
        }
    }

}