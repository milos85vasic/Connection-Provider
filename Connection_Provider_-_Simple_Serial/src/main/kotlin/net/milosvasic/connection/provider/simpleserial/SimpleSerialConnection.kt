package net.milosvasic.connection.provider.simpleserial

import net.milosvasic.connection.provider.commons.DataCallback
import net.milosvasic.connection.provider.commons.DataConnection
import java.io.*

public class SimpleSerialConnection internal constructor(
        callback: DataCallback,
        internal val comPort: String,
        internal val comPortOut: String?
) : DataConnection(callback) {

    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null

    override fun connect() {
        if (inputStream != null || outputStream != null) {
            throw IllegalStateException("Already connected")
        }
        val fileIn = File(comPort)
        if (fileIn.exists()) {
            inputStream = BufferedInputStream(FileInputStream(fileIn))
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