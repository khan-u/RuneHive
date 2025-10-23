package com.runehive.game.world.entity.combat.strategy.npc.boss.kril;

import com.runehive.game.world.entity.combat.hit.CombatHit;
import com.runehive.game.world.entity.combat.projectile.CombatProjectile;
import com.runehive.game.world.entity.combat.strategy.npc.NpcRangedStrategy;
import com.runehive.game.world.entity.mob.Mob;
import com.runehive.game.world.entity.mob.npc.Npc;

public class Zakln extends NpcRangedStrategy {

    public Zakln() {
        super(CombatProjectile.getDefinition("EMPTY"));
    }

    @Override
    public CombatHit[] getHits(Npc attacker, Mob defender) {
        return new CombatHit[] { nextRangedHit(attacker, defender, 21) };
    }

}
