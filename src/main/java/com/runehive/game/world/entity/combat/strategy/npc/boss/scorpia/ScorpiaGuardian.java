package com.runehive.game.world.entity.combat.strategy.npc.boss.scorpia;

import com.runehive.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import com.runehive.game.world.entity.mob.Mob;
import com.runehive.game.world.entity.mob.npc.Npc;

public class ScorpiaGuardian extends NpcMeleeStrategy {

    @Override
    public boolean canAttack(Npc attacker, Mob defender) {
        return false;
    }

}
