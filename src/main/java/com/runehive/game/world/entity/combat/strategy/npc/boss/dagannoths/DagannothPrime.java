package com.runehive.game.world.entity.combat.strategy.npc.boss.dagannoths;

import com.runehive.game.world.entity.combat.attack.FightType;
import com.runehive.game.world.entity.combat.hit.CombatHit;
import com.runehive.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import com.runehive.game.world.entity.mob.Mob;
import com.runehive.game.world.entity.mob.npc.Npc;

import static com.runehive.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

/** @author Michael | Chex */
public class DagannothPrime extends NpcMagicStrategy {

    public DagannothPrime() {
        super(getDefinition("Water Wave"));
    }

    @Override
    public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
        return attacker.definition.getAttackDelay();
    }

    @Override
    public CombatHit[] getHits(Npc attacker, Mob defender) {
        return new CombatHit[] { nextMagicHit(attacker, defender, 50) };
    }

}
