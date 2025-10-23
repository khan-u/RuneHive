package org.jire.runehiveps

import com.runehive.game.task.Task
import com.runehive.game.world.World

/**
 * @author Jire
 */
object WorldTask {

    inline fun schedule(delay: Int = 1, crossinline execute: () -> Unit) {
        if (delay > 0) {
            World.schedule(object : Task(delay) {
                override fun execute() {
                    cancel()
                    execute()
                }
            })
        } else execute()
    }

    @JvmStatic
    fun schedule(delay: Int = 1, execute: Runnable) {
        if (delay > 0) {
            World.schedule(object : Task(delay) {
                override fun execute() {
                    cancel()
                    execute.run()
                }
            })
        } else execute.run()
    }

}