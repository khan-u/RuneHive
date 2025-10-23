package com.runehive.game.world.entity.combat;

import com.runehive.game.world.entity.combat.hit.Hit;
import com.runehive.game.world.entity.mob.Mob;

import java.util.List;

/**
 * Represents a combat impact hit effect.
 * @author Artem Batutin
 */
public interface CombatImpact {
	
	/**
	 * Condition if the impact is affecting.
	 */
	default boolean canAffect(Mob attacker, Mob defender, Hit hit) {
		return true;
	}
	
	/**
	 * The impact execution.
	 */
	void impact(Mob attacker, Mob defender, Hit hit, List<Hit> hits);
}
