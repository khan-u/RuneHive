package com.runehive.game.engine.sync.task;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.out.SendNpcUpdate;
import com.runehive.net.packet.out.SendPlayerUpdate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class PlayerUpdateTask extends SynchronizationTask {

    private static final Logger logger = LogManager.getLogger(PlayerUpdateTask.class);

    private final Player player;

    public PlayerUpdateTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        try {
            if (player == null) {
                return;
            }

            player.send(new SendPlayerUpdate());
        } catch (Exception ex) {
            logger.fatal(String.format("Error in %s %s", PlayerUpdateTask.class.getSimpleName(), player), ex);
        }
    }

}
