package com.runehive.content.skill.impl.hunter.trap;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.object.GameObject;

public class SnareTrap extends Trap {

    private TrapState state;

    public SnareTrap(GameObject obj, TrapState state, int ticks, Player p) {
        super(obj, state, ticks, p);
    }

    public TrapState getState() {
        return state;
    }

    public void setState(TrapState state) {
        this.state = state;
    }
}

