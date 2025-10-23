package com.runehive.content.bot.objective.impl;

import com.runehive.content.bot.BotUtility;
import com.runehive.content.bot.PlayerBot;
import com.runehive.content.bot.objective.BotObjectiveListener;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.util.Utility;

public class CombatObjective implements BotObjectiveListener {

    @Override
    public void init(PlayerBot bot) {
        Player opponent = (Player) bot.getCombat().getLastAggressor();
        bot.botClass.initCombat(opponent, bot);
        bot.getCombat().attack(opponent);
        bot.speak(Utility.randomElement(BotUtility.FIGHT_START_MESSAGES));
        bot.opponent = opponent;
    }

    @Override
    public void finish(PlayerBot bot) {
    }

}
