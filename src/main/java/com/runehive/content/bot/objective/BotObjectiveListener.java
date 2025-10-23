package com.runehive.content.bot.objective;

import com.runehive.content.bot.PlayerBot;

public interface BotObjectiveListener {

    void init(PlayerBot bot);

    void finish(PlayerBot bot);

}
