package com.runehive.content.activity.infernomobs;

import com.runehive.game.world.entity.combat.attack.FightType;
import com.runehive.game.world.entity.combat.hit.CombatHit;
import com.runehive.game.world.entity.combat.strategy.npc.MultiStrategy;
import com.runehive.game.world.entity.combat.strategy.npc.NpcRangedStrategy;
import com.runehive.game.world.entity.mob.Mob;
import com.runehive.game.world.entity.mob.npc.Npc;

import static com.runehive.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

public class AkXil extends MultiStrategy {

	public AkXil() {
		currentStrategy = new Ranged();
	}

	@Override
	public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
		return attacker.definition.getAttackDelay();
	}

	private static class Ranged extends NpcRangedStrategy {
		private Ranged() {
			super(getDefinition("jalak xil"));
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { nextRangedHit(attacker, defender, 18) };
		}
	}

}
