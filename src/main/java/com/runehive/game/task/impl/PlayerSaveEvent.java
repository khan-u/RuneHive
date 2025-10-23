package com.runehive.game.task.impl;

import com.runehive.content.clanchannel.ClanRepository;
import com.runehive.game.task.Task;
import com.runehive.game.world.World;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.entity.mob.player.persist.PlayerSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerSaveEvent extends Task {
    private static final Logger logger = LogManager.getLogger(PlayerSaveEvent.class);

    public PlayerSaveEvent() {
        super(1000);
    }

    @Override
    public void execute() {
        if (World.update.get()) {
            return;
        }

        int count = 0;
        for (Player player : World.getPlayers()) {
            if (player != null && !player.isBot) {
                PlayerSerializer.save(player);
                count++;
            }
        }

        if (count != 0) {

            if (count > 10) {
                // DiscordPlugin.sendSimpleMessage("There are currently " + count + " players online!");
            }

            logger.info(count + " players were saved.");
            ClanRepository.saveAllActiveClans();
        }
    }
}
