package com.runehive.game.world.entity.mob.movement.waypoint;

import com.runehive.game.world.entity.mob.Mob;
import com.runehive.game.world.Interactable;
import com.runehive.util.Utility;

public class WalkToWaypoint extends Waypoint {
    private Runnable onDestination;

    public WalkToWaypoint(Mob mob, Interactable target, Runnable onDestination) {
        super(mob, target);
        this.onDestination = onDestination;
    }

    @Override
    public void onDestination() {
        mob.movement.reset();
        mob.face(Utility.findBestInside(mob, target));
        onDestination.run();
        cancel();
    }

}
