package com.runehive.game.world.entity.combat.attack.listener.npc.dragon;

import com.runehive.game.Animation;
import com.runehive.game.UpdatePriority;
import com.runehive.game.world.entity.combat.attack.FightType;
import com.runehive.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import com.runehive.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.runehive.game.world.entity.combat.hit.CombatHit;
import com.runehive.game.world.entity.combat.strategy.CombatStrategy;
import com.runehive.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import com.runehive.game.world.entity.combat.strategy.npc.impl.DragonfireStrategy;
import com.runehive.game.world.entity.mob.Mob;
import com.runehive.game.world.entity.mob.npc.Npc;

import static com.runehive.game.world.entity.combat.CombatUtil.createStrategyArray;
import static com.runehive.game.world.entity.combat.CombatUtil.randomStrategy;
import static com.runehive.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

/** @author Daniel */
@NpcCombatListenerSignature(npcs = {467})
public class SkeletalWyvern extends SimplifiedListener<Npc> {
    private static DragonfireStrategy DRAGONFIRE;
    private static CombatStrategy<Npc>[] STRATEGIES;

    static {
        try {
            DRAGONFIRE = new DragonfireStrategy(getDefinition("Skeletal wyvern breathe"));
            STRATEGIES = createStrategyArray(new Melee(),  DRAGONFIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean canAttack(Npc attacker, Mob defender) {
        if (!NpcMeleeStrategy.get().withinDistance(attacker, defender)) {
            attacker.setStrategy(DRAGONFIRE);
        }
        return attacker.getStrategy().canAttack(attacker, defender);
    }

    @Override
    public void finishOutgoing(Npc attacker, Mob defender) {
        if (!NpcMeleeStrategy.get().withinDistance(attacker, defender)) {
            attacker.setStrategy(DRAGONFIRE);
        } else {
            attacker.setStrategy(randomStrategy(STRATEGIES));
        }
    }

    private static final class Melee extends NpcMeleeStrategy {
        private static final Animation ANIMATION = new Animation(422, UpdatePriority.HIGH);

        @Override
        public int getAttackDistance(Npc attacker, FightType fightType) {
            return 1;
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return ANIMATION;
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            return new CombatHit[]{nextMeleeHit(attacker, defender)};
        }
    }
}
