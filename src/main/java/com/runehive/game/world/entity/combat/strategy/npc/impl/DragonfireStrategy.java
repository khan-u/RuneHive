package com.runehive.game.world.entity.combat.strategy.npc.impl;

import com.runehive.game.Animation;
import com.runehive.game.UpdatePriority;
import com.runehive.game.world.entity.combat.CombatUtil;
import com.runehive.game.world.entity.combat.attack.FightType;
import com.runehive.game.world.entity.combat.hit.CombatHit;
import com.runehive.game.world.entity.combat.projectile.CombatProjectile;
import com.runehive.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import com.runehive.game.world.entity.mob.Mob;
import com.runehive.game.world.entity.mob.npc.Npc;

public class DragonfireStrategy extends NpcMagicStrategy {
	
	public DragonfireStrategy(CombatProjectile projectileDefinition) {
		super(projectileDefinition);
	}

	@Override
	public Animation getAttackAnimation(Npc attacker, Mob defender) {
		return new Animation(81, UpdatePriority.VERY_HIGH);
	}
	
	@Override
	public int getAttackDistance(Npc attacker, FightType fightType) {
		return 1;
	}
	
	@Override
	public CombatHit[] getHits(Npc attacker, Mob defender) {
		return new CombatHit[] { CombatUtil.generateDragonfire(attacker, defender, 60, true) };
	}

}