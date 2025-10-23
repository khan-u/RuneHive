package com.runehive.content.wintertodt;

import com.runehive.game.action.Action;
import com.runehive.game.action.policy.WalkablePolicy;
import com.runehive.game.world.entity.mob.player.Player;

public class WintertodtAction extends Action<Player> {

    public WintertodtAction(Player player) {
        super(player, 1);
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.NON_WALKABLE;
    }

    @Override
    public String getName() {
        return "Wintertodt interuptable action";
    }

    @Override
    protected void execute() {

    }
}
