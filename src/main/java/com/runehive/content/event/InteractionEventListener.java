package com.runehive.content.event;

import com.runehive.game.world.entity.mob.player.Player;

public interface InteractionEventListener {

    boolean onEvent(Player player, InteractionEvent interactionEvent);
}