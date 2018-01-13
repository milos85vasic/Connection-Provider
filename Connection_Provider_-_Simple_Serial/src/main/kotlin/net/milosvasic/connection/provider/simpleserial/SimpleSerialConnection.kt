package net.milosvasic.connection.provider.simpleserial

import net.milosvasic.connection.provider.DataCallback
import net.milosvasic.connection.provider.DataConnection
import java.io.*

public class SimpleSerialConnection internal constructor(
        val comPort: String,
        val callback: DataCallback,
        val comPortOut: String? = null
) : DataConnection(callback) {

    override fun write(data: ByteArray) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null

//    override val input: InputStream
//        get() : InputStream {
//            inputStream?.let {
//                return@let
//            }
//            val file = File(comPort)
//            if (file.exists()) {
//                return BufferedInputStream(FileInputStream(file))
//            }
//            throw IllegalArgumentException("Unable to initialize input connection to: $comPort")
//        }
//
//    override val output: OutputStream
//        get() : OutputStream {
//            outputStream?.let {
//                return@let
//            }
//            comPortOut?.let {
//                val file = File(comPortOut)
//                if (file.exists()) {
//                    return BufferedOutputStream(FileOutputStream(file))
//                }
//                throw IllegalArgumentException("Unable to initialize output connection to: $comPortOut")
//            }
//            val file = File(comPort)
//            if (file.exists()) {
//                return BufferedOutputStream(FileOutputStream(file))
//            }
//            throw IllegalArgumentException("Unable to initialize output connection to: $comPort")
//        }

}