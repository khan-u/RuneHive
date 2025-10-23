package com.runehive.game.world.entity.combat.strategy.basic;

import com.runehive.content.activity.Activity;
import com.runehive.content.activity.impl.kraken.KrakenActivity;
import com.runehive.game.world.entity.combat.attack.FightType;
import com.runehive.game.world.entity.combat.hit.Hit;
import com.runehive.game.world.entity.combat.strategy.CombatStrategy;
import com.runehive.game.world.entity.mob.Mob;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.entity.skill.Skill;
import com.runehive.game.world.pathfinding.path.SimplePathChecker;
import com.runehive.util.Utility;

/**
 * @author Michael | Chex
 */
public abstract class RangedStrategy<T extends Mob> extends CombatStrategy<T> {

    @Override
    public boolean withinDistance(T attacker, Mob defender) {
        if (attacker.isPlayer() && Activity.evaluate(attacker.getPlayer(), it -> it instanceof KrakenActivity)) {
            return true;
        }

        FightType fightType = attacker.getCombat().getFightType();

        int distance = getAttackDistance(attacker, fightType);

        return Utility.inRange(attacker, defender, distance)
                && (SimplePathChecker.checkProjectile(attacker, defender)
                || SimplePathChecker.checkProjectile(defender, attacker));
    }

    protected static void addCombatExperience(Player player, Hit... hits) {
        int exp = 0;
        for (Hit hit : hits) {
            if (hit.getDamage() <= 0) continue;
            exp += hit.getDamage();
        }

        exp *= player.experienceRate;
        if (player.getCombat().getFightType() == FightType.FLARE) {
            exp *= 4;
            player.skills.addExperience(Skill.HITPOINTS, exp / 3);
            player.skills.addExperience(Skill.RANGED, exp);
        } else if (player.getCombat().getFightType() == FightType.SCORCH) {
            exp *= 4;
            player.skills.addExperience(Skill.HITPOINTS, exp / 3);
            player.skills.addExperience(Skill.STRENGTH, exp);
        } else if (player.getCombat().getFightType() == FightType.BLAZE) {
            exp *= 2;
            player.skills.addExperience(Skill.HITPOINTS, exp / 3);
            player.skills.addExperience(Skill.MAGIC, exp);
        } else {
            player.skills.addExperience(Skill.HITPOINTS, exp / 3);

            switch (player.getCombat().getFightType().getStyle()) {
                case DEFENSIVE:
                    exp /= 2;
                    player.skills.addExperience(Skill.RANGED, exp);
                    player.skills.addExperience(Skill.DEFENCE, exp);
                    break;
                default:
                    player.skills.addExperience(Skill.RANGED, exp);
                    break;
            }
        }
    }

}
