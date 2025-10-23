package com.runehive.game.world.entity.combat.attack.listener.npc;

import com.runehive.game.world.entity.combat.CombatType;
import com.runehive.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import com.runehive.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.runehive.game.world.entity.combat.hit.CombatHit;
import com.runehive.game.world.entity.combat.hit.Hit;
import com.runehive.game.world.entity.combat.strategy.CombatStrategy;
import com.runehive.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import com.runehive.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import com.runehive.game.world.entity.mob.Mob;
import com.runehive.game.world.entity.mob.npc.Npc;
import com.runehive.util.Utility;

import static com.runehive.game.world.entity.combat.CombatUtil.createStrategyArray;
import static com.runehive.game.world.entity.combat.CombatUtil.randomStrategy;
import static com.runehive.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

/** @author Daniel */
@NpcCombatListenerSignature(npcs = {3129})
public class KrilTsutsaroth extends SimplifiedListener<Npc> {
    private static CombatStrategy<Npc>[] STRATEGIES;
    private static final String[] SHOUTS = { "Attack them, you dogs!", "Attack!", "YARRRRRRRR!", "Rend them limb from limb!", "Forward!", "No retreat!", };


    static {
        try {
            STRATEGIES = createStrategyArray(NpcMeleeStrategy.get(), new MagicAttack());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Npc attacker, Mob defender, Hit[] hits) {
        attacker.setStrategy(randomStrategy(STRATEGIES));
        if (Utility.random(3) == 0) {
            attacker.speak(Utility.randomElement(SHOUTS));
        }
    }

    @Override
    public void block(Mob attacker, Npc defender, Hit hit, CombatType combatType) {
        attacker.getStrategy().block(attacker, defender, hit, combatType);
        defender.getCombat().attack(attacker);
    }

    private static class MagicAttack extends NpcMagicStrategy {
        public MagicAttack() {
            super(getDefinition("Kril Tsutsaroth"));
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit hit = nextMagicHit(attacker, defender, 30);
            hit.setAccurate(true);
            return new CombatHit[]{hit};
        }
    }
}
