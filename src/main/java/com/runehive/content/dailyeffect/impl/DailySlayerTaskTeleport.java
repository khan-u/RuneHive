package com.runehive.content.dailyeffect.impl;

import com.runehive.content.dailyeffect.DailyEffect;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.entity.mob.player.PlayerRight;

public class DailySlayerTaskTeleport extends DailyEffect {

    @Override
    public int maxUses(Player player) {
        if (PlayerRight.isElite(player)) {
            return 10;
        } else if (PlayerRight.isExtreme(player)) {
            return 8;
        } else if (PlayerRight.isSuper(player)) {
            return 6;
        } else if (PlayerRight.isDonator(player)) {
            return 4;
        }
        return 2;
    }
}
