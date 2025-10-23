package com.runehive.game.world.entity.combat.attack.listener.item.voidknight.elite;

import com.runehive.game.world.entity.combat.CombatType;
import com.runehive.game.world.entity.combat.attack.listener.ItemCombatListenerSignature;
import com.runehive.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.runehive.game.world.entity.mob.Mob;
import com.runehive.game.world.entity.mob.player.Player;

/**
 * Handles the slayer helmet / black mask effects ofr slayer tasks.
 *
 * @author Michael | Chex
 */
@ItemCombatListenerSignature(requireAll = true, items = { 11664, 13072, 13073, 8842 })
public class EliteKnightRangedListener extends SimplifiedListener<Player> {

    @Override
    public int modifyRangedLevel(Player attacker, Mob defender, int level) {
        if (attacker.getStrategy().getCombatType() != CombatType.RANGED)
            return level;
        return level * 11 / 10;
    }

    @Override
    public int modifyDamage(Player attacker, Mob defender, int damage) {
        if (attacker.getStrategy().getCombatType() != CombatType.RANGED)
            return damage;
        return damage * 41 / 40;
    }

}
