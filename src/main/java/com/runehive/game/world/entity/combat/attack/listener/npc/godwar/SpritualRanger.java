package com.runehive.game.world.entity.combat.attack.listener.npc.godwar;

import com.runehive.game.Animation;
import com.runehive.game.UpdatePriority;
import com.runehive.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import com.runehive.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.runehive.game.world.entity.combat.attack.listener.npc.Dessourt;
import com.runehive.game.world.entity.combat.hit.Hit;
import com.runehive.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import com.runehive.game.world.entity.combat.strategy.npc.NpcRangedStrategy;
import com.runehive.game.world.entity.mob.Mob;
import com.runehive.game.world.entity.mob.npc.Npc;

import static com.runehive.game.world.entity.combat.CombatUtil.createStrategyArray;
import static com.runehive.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

/**
 * @author Daniel
 */
@NpcCombatListenerSignature(npcs = {3160, 2211})
public class SpritualRanger extends SimplifiedListener<Npc> {

    private static RangedAttack RANGED = new RangedAttack();

    @Override
    public void start(Npc attacker, Mob defender, Hit[] hits) {
        attacker.setStrategy(RANGED);
    }

    private static class RangedAttack extends NpcRangedStrategy {
        private RangedAttack() {
            super(getDefinition("Spirtual Ranger"));
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return new Animation(426, UpdatePriority.VERY_HIGH);
        }
    }
}
