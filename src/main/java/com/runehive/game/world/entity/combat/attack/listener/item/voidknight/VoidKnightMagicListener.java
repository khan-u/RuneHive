package com.runehive.game.world.entity.combat.attack.listener.item.voidknight;

import com.runehive.game.world.entity.combat.attack.listener.ItemCombatListenerSignature;
import com.runehive.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.runehive.game.world.entity.mob.Mob;
import com.runehive.game.world.entity.mob.player.Player;

/**
 * Handles the slayer helmet / black mask effects ofr slayer tasks.
 *
 * @author Michael | Chex
 */
@ItemCombatListenerSignature(requireAll = true, items = { 11663, 8839, 8840, 8842 })
public class VoidKnightMagicListener extends SimplifiedListener<Player> {

    @Override
    public int modifyMagicLevel(Player attacker, Mob defender, int level) {
        return level * 29 / 20;
    }

}
