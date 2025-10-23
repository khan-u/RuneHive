package com.runehive.content.wintertodt.actions;

import com.runehive.game.action.Action;
import com.runehive.game.action.policy.WalkablePolicy;
import com.runehive.game.world.entity.mob.npc.Npc;
import com.runehive.game.world.position.Position;

public class FlameWalk extends Action<Npc> {

    public FlameWalk(Npc mob) {
        super(mob, 1);
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.WALKABLE;
    }

    @Override
    public String getName() {
        return "Walk flame";
    }

    @Override
    protected void execute() {
        if(getMob().getPosition().equals(new Position(1630, 4006)) || getMob().getPosition().equals(new Position(1629, 4007)) || getMob().getPosition().equals(new Position(1631, 4007))) {
            getMob().action.getCurrentAction().cancel();
            getMob().unregister();
        }
    }
}
