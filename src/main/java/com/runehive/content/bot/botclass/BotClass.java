package com.runehive.content.bot.botclass;

import com.runehive.content.bot.PlayerBot;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.items.Item;

public interface BotClass {

    Item[] inventory();

    Item[] equipment();

    int[] skills();

    void initCombat(Player target, PlayerBot bot);

    void handleCombat(Player target, PlayerBot bot);

    void endFight(PlayerBot bot);

    void pot(Player target, PlayerBot bot);

    void eat(Player target, PlayerBot bot);

}
