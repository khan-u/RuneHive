package com.runehive.game.world.entity.combat.strategy.npc.boss.scorpia;

import com.runehive.game.world.entity.combat.attack.FightType;
import com.runehive.game.world.entity.combat.hit.Hit;
import com.runehive.game.world.entity.combat.projectile.CombatProjectile;
import com.runehive.game.world.entity.combat.strategy.npc.NpcRangedStrategy;
import com.runehive.game.world.entity.mob.Mob;
import com.runehive.game.world.entity.mob.npc.Npc;
import com.runehive.game.world.entity.skill.Skill;

public class ScorpiaOffspring extends NpcRangedStrategy {

    public ScorpiaOffspring() {
        super(CombatProjectile.getDefinition("EMPTY"));
    }

    @Override
    public void hit(Npc attacker, Mob defender, Hit hit) {
        super.hit(attacker, defender, hit);
        if (hit.getDamage() > 0) {
            defender.skills.get(Skill.PRAYER).removeLevel(1);
            defender.skills.refresh(Skill.PRAYER);
        }
    }

    @Override
    public int getAttackDistance(Npc attacker, FightType fightType) {
        return 5;
    }

}
