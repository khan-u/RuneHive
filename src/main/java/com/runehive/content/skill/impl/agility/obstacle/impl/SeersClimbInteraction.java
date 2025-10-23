package com.runehive.content.skill.impl.agility.obstacle.impl;

import com.runehive.content.skill.impl.agility.obstacle.ObstacleInteraction;
import com.runehive.game.Animation;
import com.runehive.game.task.Task;
import com.runehive.game.world.World;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.position.Position;

public interface SeersClimbInteraction extends ObstacleInteraction {

    @Override
    default void start(Player player) {
    }

    @Override
    default void onExecution(Player player, Position start, Position end) {
        player.animate(new Animation(getAnimation()));
        World.schedule(new Task(1) {
            int count = 0;

            @Override
            public void execute() {
                if (count == 0) {
                    player.move(new Position(player.getX(), player.getY(), player.getHeight() + 1));
                    player.animate(new Animation(1118));
                } else if (count == 2) {
                    player.move(end);
                    player.animate(new Animation(65535));
                    this.cancel();
                }
                count++;
            }
        });
    }

    @Override
    default void onCancellation(Player player) {
    }
}