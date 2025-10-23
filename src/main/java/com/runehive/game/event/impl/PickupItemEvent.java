package com.runehive.game.event.impl;

import com.runehive.game.event.Event;
import com.runehive.game.world.items.Item;
import com.runehive.game.world.items.ground.GroundItem;
import com.runehive.game.world.position.Position;

public class PickupItemEvent implements Event {

    private final GroundItem groundItem;

    public PickupItemEvent(GroundItem groundItem) {
        this.groundItem = groundItem;
    }

    public GroundItem getGroundItem() {
        return groundItem;
    }

    public Item getItem() {
        return groundItem.item;
    }

    public Position getPosition() {
        return groundItem.getPosition();
    }

}
