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
    private lateinit var input: File
    private lateinit var output: File

    override val executor = Executor.obtainExecutor(2)

    override fun connect() {
        executor.execute {
            if (isConnected()) {
                callback.onError("Already connected")
                return@execute
            }
            output = File(comPortOut)
            if (!output.exists()) {
                disconnect("Couldn't connect to: $comPortOut")
                return@execute
            }
            var inPath = comPortOut
            comPortIn?.let {
                inPath = comPortIn
            }
            input = File(inPath)
            if (!input.exists()) {
                disconnect("Couldn't connect to: $inPath")
                return@execute
            }
            startReading()
        }
    }

    override fun disconnect() {
        disconnect(null)
    }

    override fun write(data: ByteArray) {
        executor.execute {
            output.appendBytes(data)
            callback.onDataWritten(data)
        }
    }

    override fun isConnected() = connected.get()

    private fun startReading() {
        executor.execute {
            //            val bufferedReader: BufferedReader?
//            try {
//                bufferedReader = BufferedReader(InputStreamReader(input, StandardCharsets.UTF_8))
//            } catch (e: Exception) {
//                disconnect(e)
//                return@execute
//            }
//            while (input != null) {
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
            error?.let {
                callback.onError(error)
            }
            setConnected(false)
        }
    }

}