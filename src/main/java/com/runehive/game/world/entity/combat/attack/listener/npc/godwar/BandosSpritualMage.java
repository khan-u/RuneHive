package com.runehive.game.world.entity.combat.attack.listener.npc.godwar;

import com.runehive.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import com.runehive.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.runehive.game.world.entity.combat.hit.CombatHit;
import com.runehive.game.world.entity.combat.hit.Hit;
import com.runehive.game.world.entity.combat.strategy.CombatStrategy;
import com.runehive.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
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
@NpcCombatListenerSignature(npcs = { 2244 })
public class BandosSpritualMage extends SimplifiedListener<Npc> {

	private static MagicAttack MAGIC = new MagicAttack();
	private static CombatStrategy<Npc>[] STRATEGIES = createStrategyArray(NpcMeleeStrategy.get(), MAGIC);

	@Override
	public void start(Npc attacker, Mob defender, Hit[] hits) {
		attacker.setStrategy(randomStrategy(STRATEGIES));
	}

	private static class MagicAttack extends NpcMagicStrategy {
		private MagicAttack() {
			super(getDefinition("Spirtual Mage"));
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			CombatHit combatHit = nextMagicHit(attacker, defender, 15);
			combatHit.setAccurate(true);
			return new CombatHit[] { combatHit };
		}
	}
}
