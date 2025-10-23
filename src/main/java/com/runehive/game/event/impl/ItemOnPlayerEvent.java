package com.runehive.game.event.impl;

import com.runehive.game.event.Event;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.items.Item;

public class ItemOnPlayerEvent implements Event {

    private final Player other;

    private final Item used;

    private final int slot;

    public ItemOnPlayerEvent(Player other, Item used, int slot) {
        this.other = other;
        this.used = used;
        this.slot = slot;
    }

    public Player getOther() {
        return other;
    }

    public Item getUsed() {
        return used;
    }

    public int getSlot() {
        return slot;
    }

}
