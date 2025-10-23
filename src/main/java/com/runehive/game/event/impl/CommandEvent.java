package com.runehive.game.event.impl;

import com.runehive.game.event.Event;
import com.runehive.game.world.entity.mob.player.command.CommandParser;

public class CommandEvent implements Event {

    private final CommandParser parser;

    public CommandEvent(CommandParser parser) {
        this.parser = parser;
    }

    public CommandParser getParser() {
        return parser;
    }

}
