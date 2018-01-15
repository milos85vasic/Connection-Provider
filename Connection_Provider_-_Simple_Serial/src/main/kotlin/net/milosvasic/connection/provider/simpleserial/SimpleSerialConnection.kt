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
    private var input: InputStream? = null
    private var output: OutputStream? = null

    override val executor = Executor.obtainExecutor(2)

    override fun connect() {
        executor.execute {
            if (isConnected()) {
                callback.onError("Already connected")
                return@execute
            }
            // TODO: Init input and output stream.
            if (input != null) {
                startReading()
            } else {
                failNotConnected()
            }
        }
    }

    override fun disconnect() {
        disconnect(null)
    }

    override fun write(data: ByteArray) {
        executor.execute {
            if (output != null) {
                // TODO: Write to output stream.
            } else {
                failNotConnected()
            }
        }
    }

    override fun isConnected() = connected.get()

    private fun startReading() {
        executor.execute {
            setConnected(true)
            // TODO: Read while connected
        }
    }

    private fun setConnected(toggle: Boolean) {
        connected.set(toggle)
        callback.onConnectivityChanged(isConnected())
    }

    private fun disconnect(error: String?) {
        executor.execute {
            // TODO: Un init input and output stream.
            setConnected(false)
            error?.let {
                callback.onError(error)
            }
        }
    }

    private fun failNotConnected() {
        callback.onError("Not connected.")
    }

}