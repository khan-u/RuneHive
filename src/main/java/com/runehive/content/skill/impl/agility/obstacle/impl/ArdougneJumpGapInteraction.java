package com.runehive.content.skill.impl.agility.obstacle.impl;

import com.runehive.content.skill.impl.agility.obstacle.ObstacleInteraction;
import com.runehive.game.Animation;
import com.runehive.game.task.Task;
import com.runehive.game.world.World;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.position.Position;

public interface ArdougneJumpGapInteraction extends ObstacleInteraction {
    @Override
    default void start(Player player) {
    }

    @Override
    default void onExecution(Player player, Position start, Position end) {
        World.schedule(new Task(1) {
            int ticks = 0;

            @Override
            public void execute() {
                switch (ticks++) {
                    case 1:
                        player.face(end);
                        player.animate(new Animation(2586));
                        break;
                    case 2:
                        player.move(end);
                        player.animate(new Animation(2588));
                        cancel();
                        break;
                }
            }
        });
    }

    @Override
    default void onCancellation(Player player) {
    }
}
