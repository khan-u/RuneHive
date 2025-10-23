package com.runehive.game.world.entity.combat.attack.listener.npc.godwar;

import com.runehive.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import com.runehive.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.runehive.game.world.entity.combat.hit.Hit;
import com.runehive.game.world.entity.combat.strategy.CombatStrategy;
import com.runehive.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import com.runehive.game.world.entity.combat.strategy.npc.NpcRangedStrategy;
import com.runehive.game.world.entity.mob.Mob;
import com.runehive.game.world.entity.mob.npc.Npc;

import static com.runehive.game.world.entity.combat.CombatUtil.createStrategyArray;
import static com.runehive.game.world.entity.combat.CombatUtil.randomStrategy;
import static com.runehive.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

/**
 * @author Daniel
 */
@NpcCombatListenerSignature(npcs = { 2242 })
public class BandosSpritualRanger extends SimplifiedListener<Npc> {

	private static RangedAttack RANGED = new RangedAttack();
	private static CombatStrategy<Npc>[] STRATEGIES = createStrategyArray(NpcMeleeStrategy.get(), RANGED);

	@Override
	public void start(Npc attacker, Mob defender, Hit[] hits) {
		attacker.setStrategy(randomStrategy(STRATEGIES));
	}

	private static class RangedAttack extends NpcRangedStrategy {
		private RangedAttack() {
			super(getDefinition("Spirtual Ranger"));
		}
	}
}
