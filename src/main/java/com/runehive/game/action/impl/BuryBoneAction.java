package com.runehive.game.action.impl;

import com.runehive.Config;
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

/**
 * Handles burying a bone.
 *
 * @author Michael | Chex
 */
public final class BuryBoneAction extends SkillAction {
    private final int slot;
    private final Item item;
    private final BoneData bone;

    public BuryBoneAction(Player player, BoneData bone, int slot) {
        super(player, Optional.empty(), true);
        this.slot = slot;
        this.bone = bone;
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
        getMob().animate(new Animation(827));
        Player player = getMob().getPlayer();
        player.inventory.remove(item, slot, true);
        player.skills.addExperience(skill(), experience());
        player.send(new SendMessage("You bury the " + item.getName() + "."));
        AchievementHandler.activate(player, AchievementKey.BURY_BONES, 1);

        if (player.equipment.hasAmulet() && player.equipment.getAmuletSlot().getId() == 22111) {
            int current = player.skills.getLevel(Skill.PRAYER);
            int maximum = player.skills.getMaxLevel(Skill.PRAYER);

            if (current < maximum) {
                int points = bone.getBoneAmulet();

                if (current + points > maximum) {
                    points = maximum - current;
                }

                player.skills.get(Skill.PRAYER).addLevel(points);
                player.skills.refresh(Skill.PRAYER);
                player.message("You feel your " + player.equipment.getAmuletSlot().getName() + " vibrate as you bury the bone.");
            }
        }

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
        double exp = (bone.getExperience() * Config.PRAYER_MODIFICATION);
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
        return "Bone bury";
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