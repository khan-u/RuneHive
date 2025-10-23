package com.runehive.game.action.impl;

import com.runehive.game.Animation;
import com.runehive.game.UpdatePriority;
import com.runehive.game.action.Action;
import com.runehive.game.action.policy.WalkablePolicy;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.object.GameObject;
import com.runehive.game.world.position.Position;
import com.runehive.net.packet.out.SendMessage;
import com.runehive.util.MessageColor;

import java.util.function.Predicate;

/**
 * Created by Daniel on 2017-10-12.
 */
public class LadderAction extends Action<Player> {
    private int count;
    private final GameObject ladder;
    private final Position position;
    private final Predicate<Player> condition;
    private final String message;

    public LadderAction(Player mob, GameObject ladder, Position position) {
        this(mob, ladder, position, null, null);
    }

    public LadderAction(Player mob, GameObject ladder, Position position, Predicate<Player> condition, String message) {
        super(mob, 1, false);
        this.ladder = ladder;
        this.position = position;
        this.condition = condition;
        this.message = message;
    }

    @Override
    protected boolean canSchedule() {
        if (condition != null && !condition.test(getMob())) {
            getMob().send(new SendMessage(message, MessageColor.RED));
            return false;
        }
        return true;
    }

    @Override
    protected void onSchedule() {
        getMob().locking.lock();
        getMob().face(ladder.getPosition());
        getMob().getCombat().reset();
        getMob().damageImmunity.reset(3_000);
    }

    @Override
    public void execute() {
        if (count == 0) {
            getMob().animate(new Animation(828, UpdatePriority.VERY_HIGH));
        } else if (count == 1) {
            getMob().move(position);
            cancel();
        }
        count++;
    }

    @Override
    protected void onCancel(boolean logout) {
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.NON_WALKABLE;
    }

    @Override
    public String getName() {
        return "Ladder action";
    }

    @Override
    public boolean prioritized() {
        return false;
    }
}
