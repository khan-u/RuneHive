package com.runehive.content.dailyeffect.impl;

import com.runehive.content.dailyeffect.DailyEffect;
import com.runehive.game.world.entity.mob.player.Player;

public class DailySpellBookSwap extends DailyEffect {

    @Override
    public int maxUses(Player player) {
        return 5;
    }
}
