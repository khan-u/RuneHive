package com.runehive.game.world.entity.combat.attack.listener.item;

import com.runehive.game.world.entity.combat.CombatType;
import com.runehive.game.world.entity.combat.attack.listener.ItemCombatListenerSignature;
import com.runehive.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.runehive.game.world.entity.mob.Mob;

/**
 * Handles the Occult necklace listener.
 * OSRS Wiki: http://oldschoolrunescape.wikia.com/wiki/Occult_necklace
 *
 * @author Daniel
 */
@ItemCombatListenerSignature(requireAll = false, items = {12002, 19720})
public class OccultNecklaceListener extends SimplifiedListener<Mob> {

    @Override
    public int modifyDamage(Mob attacker, Mob defender, int damage) {
        if (attacker.getStrategy().getCombatType() != CombatType.MAGIC)
            return damage;
        return damage * 11 / 10;
    }

}
