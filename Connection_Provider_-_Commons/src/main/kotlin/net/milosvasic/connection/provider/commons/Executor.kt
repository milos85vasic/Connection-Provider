package net.milosvasic.connection.provider.commons


import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class Executor private constructor(corePoolSize: Int, maximumPoolSize: Int, queue: BlockingQueue<Runnable>
) : ThreadPoolExecutor(corePoolSize, maximumPoolSize, 0, TimeUnit.MILLISECONDS, queue) {

    companion object {
        fun obtainExecutor(capacity: Int) = Executor(capacity, capacity, LinkedBlockingDeque<Runnable>())
    }
}