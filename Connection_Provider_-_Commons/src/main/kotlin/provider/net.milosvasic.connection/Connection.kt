package provider.net.milosvasic.connection

import java.io.InputStream
import java.io.OutputStream

interface Connection {

    val input: InputStream
    val output: OutputStream



}