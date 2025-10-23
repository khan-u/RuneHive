package com.runehive.game.task.impl;

import com.runehive.Config;
import com.runehive.content.triviabot.TriviaBot;
import com.runehive.game.task.Task;
import com.runehive.game.world.World;
import com.runehive.util.Utility;

/**
 * Sends game messages to all the online players.
 *
 * @author Daniel
 */
public class MessageEvent extends Task {

    /** The message randomevent ticks. */
    private int tick;

    /** Constructs a new <code>MessageEvent</code>. */
    public MessageEvent() {
        super(180);
        this.tick = 0;
    }

    @Override
    public void execute() {
        tick++;

        if (tick % 2 == 0) {
            String message = Utility.randomElement(Config.MESSAGES);
            World.sendMessage("<img=15> <col=2C7526>Broadcast: </col>" + message);
        } else {
            TriviaBot.assign();
        }
    }
}
