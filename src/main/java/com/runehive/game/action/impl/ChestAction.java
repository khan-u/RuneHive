package com.runehive.game.action.impl;

import com.runehive.content.ActivityLog;
import com.runehive.game.world.items.Item;
import com.runehive.net.packet.out.SendMessage;
import com.runehive.game.Animation;
import com.runehive.game.action.Action;
import com.runehive.game.action.policy.WalkablePolicy;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.content.achievement.AchievementHandler;
import com.runehive.content.achievement.AchievementKey;

/**
 * Handles opening a chest.
 *
 * @author Daniel
 */
public final class ChestAction extends Action<Player> {
    private final int key;
    private final Item[] items;

    public ChestAction(Player player, int key, Item... items) {
        super(player, 1);
        this.key = key;
        this.items = items;
    }

    @Override
    protected boolean canSchedule() {
        if (!getMob().inventory.hasCapacityFor(items)) {
            getMob().message("You do not have enough free inventory spaces to do this!");
            return false;
        }
        return true;
    }

    @Override
    protected void onSchedule() {
        getMob().locking.lock(2);
        getMob().inventory.remove(key, 1);
        getMob().animate(new Animation(881));
        getMob().send(new SendMessage("You attempt to unlock the chest..."));
    }

    @Override
    public void execute() {
        cancel();
    }

    @Override
    protected void onCancel(boolean logout) {
        getMob().inventory.addOrDrop(items);
        getMob().send(new SendMessage("...you find a few items inside of the chest.", true));

        if (key == 989) {
            getMob().activityLogger.add(ActivityLog.CRYSTAL_CHEST);
            AchievementHandler.activate(getMob(), AchievementKey.CRYSTAL_CHEST, 1);
        } else if (key == 20608) {
            getMob().activityLogger.add(ActivityLog.BLOOD_MONEY_CHEST);
        }
    }

    @Override
    public String getName() {
        return "chest action";
    }

    @Override
    public boolean prioritized() {
        return false;
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.NON_WALKABLE;
    }
}