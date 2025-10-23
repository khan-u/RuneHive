package com.runehive.game.world.entity.combat.attack.listener.item;

import com.runehive.Config;
import com.runehive.net.packet.out.SendMessage;
import com.runehive.game.world.entity.combat.CombatType;
import com.runehive.game.world.entity.combat.attack.listener.ItemCombatListenerSignature;
import com.runehive.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.runehive.game.world.entity.combat.hit.Hit;
import com.runehive.game.world.entity.mob.Mob;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.content.skill.impl.magic.teleport.Teleportation;
import com.runehive.game.world.position.Area;

/**
 * Handles the ring of life listener.
 * OSRS Wiki: http://oldschoolrunescape.wikia.com/wiki/Ring_of_life
 *
 * @author Daniel
 */
@ItemCombatListenerSignature(requireAll = false, items = {2570, 9753, 9754})
public class RingOfLifeListener extends SimplifiedListener<Player> {

    @Override
    public void block(Mob attacker, Player defender, Hit hit, CombatType combatType) {
        if (Area.inDuelArena(defender))
            return;
        if (defender.getCurrentHealth() - hit.getDamage() <= 0)
            return;
        if (defender.getCurrentHealth() - hit.getDamage() <= defender.getMaximumHealth() * 0.10) {
            if (Teleportation.teleport(defender, Config.DEFAULT_POSITION)) {
                defender.send(new SendMessage("The Ring of life has saved you; but was destroyed in the process."));
            }
            defender.getCombat().removeListener(this);
            if (defender.equipment.contains(2570)) {
                defender.equipment.remove(2570);
            }
        }
    }
}
