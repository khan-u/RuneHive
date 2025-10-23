package com.runehive.game.action.impl;

import com.runehive.Config;
import com.runehive.game.action.Action;
import com.runehive.game.action.policy.WalkablePolicy;
import com.runehive.game.world.entity.mob.Direction;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.entity.skill.Skill;
import com.runehive.game.world.position.Position;

public class RevShortcutAction extends Action<Player> {

    int tick, xp, x, y;
    Direction direction;
    public RevShortcutAction(Player player, Direction direction, int xp) {
        super(player, 1);
        this.direction = direction;
        this.xp = xp;

        if(direction == Direction.SOUTH) {
            x = 0;
            y = -2;
        } else if(direction == Direction.NORTH) {
            x = 0;
            y = 2;
        } else if(direction == Direction.EAST) {
            x = 2;
            y = 0;
        } else if(direction == Direction.WEST) {
            x = -2;
            y = 0;
        }
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.NON_WALKABLE;
    }

    @Override
    public String getName() {
        return "Rev shortcut";
    }

    @Override
    protected void execute() {
        final Player p = getMob();
        if(tick == 0 || tick == 2) {
            p.forceMove(1, 741, 0, 30, new Position(x, y), direction);
        } else if(tick == 1 || tick == 3) {
            p.move(new Position((p.getPosition().getX() + x), (p.getPosition().getY() + y)));
        }
        if(tick == 3) {
            p.skills.addExperience(Skill.AGILITY, xp * Config.AGILITY_MODIFICATION);
            cancel();
        }

        tick++;
    }

    @Override
    public boolean cancellableInProgress() {
        return false;
    }

}
