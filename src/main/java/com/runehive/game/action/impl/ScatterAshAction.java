package com.runehive.game.action.impl;

import com.runehive.Config;
import com.runehive.content.skill.impl.prayer.AshData;
import com.runehive.content.skillcape.SkillCape;
import com.runehive.content.achievement.AchievementHandler;
import com.runehive.content.achievement.AchievementKey;
import com.runehive.content.skill.SkillAction;
import com.runehive.content.skill.impl.prayer.BoneData;
import com.runehive.game.Animation;
import com.runehive.game.action.policy.WalkablePolicy;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.entity.skill.Skill;
import com.runehive.game.world.items.Item;
import com.runehive.net.packet.out.SendMessage;

import java.util.Optional;

public final class ScatterAshAction extends SkillAction {
    private final int slot;
    private final Item item;
    private final AshData ashes;

    public ScatterAshAction(Player player, AshData ashes, int slot) {
        super(player, Optional.empty(), true);
        this.slot = slot;
        this.ashes = ashes;
        this.item = player.inventory.get(slot);
    }

    @Override
    public boolean canInit() {
        return getMob().skills.getSkills()[skill()].stopwatch.elapsed(1200);
    }

    @Override
    public void init() {

    }

    @Override
    public void onExecute() {
        getMob().animate(new Animation(2295));
        Player player = getMob().getPlayer();
        player.inventory.remove(item, slot, true);
        player.skills.addExperience(skill(), experience());
        player.send(new SendMessage("You scatter the ashes."));

        cancel();
    }

    @Override
    public void onCancel(boolean logout) {
        getMob().skills.getSkills()[skill()].stopwatch.reset();
    }

    @Override
    public Optional<SkillAnimation> animation() {
        return Optional.empty();
    }

    @Override
    public double experience() {
        double exp = (ashes.getExperience() * Config.PRAYER_MODIFICATION);
        if (SkillCape.isEquipped(getMob().getPlayer(), SkillCape.PRAYER)) {
            exp *= 2.0;
        }
        return exp;
    }

    @Override
    public int skill() {
        return Skill.PRAYER;
    }

    @Override
    public String getName() {
        return "Ash scatter";
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