package com.runehive.content.skill.impl.agility.obstacle.impl;

import com.runehive.content.skill.impl.agility.obstacle.ObstacleInteraction;
import com.runehive.game.Animation;
import com.runehive.game.task.Task;
import com.runehive.game.world.World;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.position.Position;

public interface ArdougneRoofJumpInteraction2 extends ObstacleInteraction {
    @Override
    default void start(Player player) {
        player.face(new Position(2658, 3298));
    }

    @Override
    default void onExecution(Player player, Position start, Position end) {
        World.schedule(new Task(1) {
            int ticks = 0;
            @Override
            public void execute() {
                switch (ticks++) {
                    case 1:
                        player.animate(new Animation(2586));
                        player.move(new Position(2658, 3298, 1));
                        break;
                    case 2:
                        player.face(new Position(2658, 3298));
                        player.animate(new Animation(2588));
                        break;
                    case 3:
                        player.movement.walkTo(3, 0);
                        break;
                    case 7:
                        player.face(new Position(2663, 3296));
                        player.animate(new Animation(2586));
                        break;
                    case 8:
                        player.animate(new Animation(2588));
                        player.move(new Position(2663, 3297, 1));
                        break;
                    case 9:
                        player.movement.walkTo(3, 0);
                        break;
                    case 13:
                        player.animate(new Animation(2586));
                        break;
                    case 14:
                        player.move(end);
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