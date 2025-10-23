package com.runehive.game.event.impl;

import com.runehive.game.event.Event;
import com.runehive.game.world.position.Position;

public class MovementEvent implements Event {

    private final Position destination;

    public MovementEvent(Position destination) {
        this.destination = destination;
    }

    public Position getDestination() {
        return destination;
    }

}
