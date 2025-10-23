package org.jire.runehiveps.event.npc

import com.runehive.game.world.entity.mob.npc.Npc
import com.runehive.game.world.entity.mob.player.Player

/**
 * @author Jire
 */
class AttackNpcEvent(override val slot: Int) : NpcClickEvent {

    override fun handleNpc(player: Player, npc: Npc) {
        player.combat.attack(npc)
    }

}