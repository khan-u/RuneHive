package com.runehive.game.world.entity.combat.attack.listener.npc;

import com.runehive.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import com.runehive.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.runehive.game.world.entity.combat.hit.Hit;
import com.runehive.game.world.entity.mob.Mob;
import com.runehive.game.world.entity.mob.npc.Npc;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.util.Utility;

/**
 * @author Daniel
 */
@NpcCombatListenerSignature(npcs = {2216})
public class SergeantStrongstack extends SimplifiedListener<Npc> {

    @Override
    public void hit(Npc attacker, Mob defender, Hit hit) {
        if (!defender.isPlayer())
            return;
        if (Utility.random(10) != 0)
            return;

        Player playerDefender = defender.getPlayer();

        if (playerDefender.viewport.getPlayersInViewport().size() < 1)
            return;

        Player[] players = new Player[playerDefender.viewport.getPlayersInViewport().size()];
        int index = 0;

        for (Player player :playerDefender.viewport.getPlayersInViewport()) {
            if (attacker.getCombat().isAttacking(player))
                continue;
            if (!Utility.within(attacker, player, 7))
                continue;
            players[index] = player;
            index++;
        }

        Player next = Utility.randomElement(players);
        if (next != null)
            attacker.getCombat().attack(next);
    }
}
