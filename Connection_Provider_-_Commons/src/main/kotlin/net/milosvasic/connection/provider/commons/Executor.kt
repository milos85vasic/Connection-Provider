package net.milosvasic.connection.provider.commons


import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

internal class Executor private constructor(corePoolSize: Int, maximumPoolSize: Int, queue: BlockingQueue<Runnable>)
    : ThreadPoolExecutor(corePoolSize, maximumPoolSize, 0, TimeUnit.MILLISECONDS, queue) {

    companion object {
        private val executor = Executor(2, 2, LinkedBlockingDeque<Runnable>())

        fun execute(runnable: Runnable) {
            executor.execute(runnable)
        }
    }
}