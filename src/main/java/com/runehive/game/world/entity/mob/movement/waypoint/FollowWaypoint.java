package com.runehive.game.world.entity.mob.movement.waypoint;

import com.runehive.game.world.entity.mob.Mob;

public class FollowWaypoint extends Waypoint {

    public FollowWaypoint(Mob mob, Mob target) {
        super(mob, target);
    }

    @Override
    public void onDestination() {
        mob.movement.reset();
    }

}
