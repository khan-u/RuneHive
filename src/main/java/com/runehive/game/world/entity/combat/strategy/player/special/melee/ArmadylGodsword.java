package com.runehive.game.world.entity.combat.strategy.player.special.melee;

import com.runehive.content.achievement.AchievementHandler;
import com.runehive.content.achievement.AchievementKey;
import com.runehive.game.Animation;
import com.runehive.game.Graphic;
import com.runehive.game.UpdatePriority;
import com.runehive.game.world.entity.combat.hit.Hit;
import com.runehive.game.world.entity.combat.strategy.player.PlayerMeleeStrategy;
import com.runehive.game.world.entity.mob.Mob;
import com.runehive.game.world.entity.mob.player.Player;

/** @author Michael | Chex */
public class ArmadylGodsword extends PlayerMeleeStrategy {

    //AGS(normal): 7644, AGS(OR): 7645
    private static final Animation ANIMATION = new Animation(7644, UpdatePriority.HIGH);
    private static final Graphic GRAPHIC = new Graphic(1211);

    private static final ArmadylGodsword INSTANCE = new ArmadylGodsword();

    private ArmadylGodsword() {
    }

    @Override
    public void start(Player attacker, Mob defender, Hit[] hits) {
        super.start(attacker, defender, hits);
        attacker.animate(ANIMATION, true);
        attacker.graphic(GRAPHIC);
    }

    @Override
    public void hitsplat(Player attacker, Mob defender, Hit hit) {
        super.hitsplat(attacker, defender, hit);
        if (defender.isPlayer() && hit.getDamage() >= 80) {
            AchievementHandler.activate(attacker, AchievementKey.AGS_MAX);
        }
    }

    @Override
    public void onKill(Player attacker, Mob defender, Hit hit) {
        if (defender.isPlayer()) {
            AchievementHandler.activate(attacker, AchievementKey.AGS_SPEC);
        }
    }

    @Override
    public Animation getAttackAnimation(Player attacker, Mob defender) {
        return ANIMATION;
    }

    @Override
    public int modifyAccuracy(Player attacker, Mob defender, int roll) {
        return 2 * roll;
    }

    @Override
    public int modifyDamage(Player attacker, Mob defender, int damage) {
        return (int) (damage * 1.375);
    }

    public static ArmadylGodsword get() {
        return INSTANCE;
    }

}