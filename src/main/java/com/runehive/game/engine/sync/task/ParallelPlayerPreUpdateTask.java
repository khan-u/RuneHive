package com.runehive.game.engine.sync.task;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.session.GameSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParallelPlayerPreUpdateTask extends SynchronizationTask {

    private static final Logger logger = LogManager.getLogger(ParallelPlayerPreUpdateTask.class);

    private final Player player;

    public ParallelPlayerPreUpdateTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        try {

        } catch (Exception ex) {
            logger.error(String.format("Error in %s. player=%s", PlayerPreUpdateTask.class.getSimpleName(), player), ex);
        }
    }

}
