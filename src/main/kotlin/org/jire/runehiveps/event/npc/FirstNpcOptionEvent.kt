package org.jire.runehiveps.event.npc

import com.runehive.content.event.impl.FirstNpcClick
import com.runehive.game.world.entity.mob.npc.Npc
import com.runehive.game.world.entity.mob.player.Player

/**
 * @author Jire
 */
class FirstNpcOptionEvent(slot: Int) : NpcOptionEvent(slot, 0) {

    override fun handleNpc(player: Player, npc: Npc) {
        if (npc.id == 394 && player.position.isWithinDistance(npc.position, 2)) {
            publishToPluginManager(player, npc)
        } else {
            super.handleNpc(player, npc)
        }
    }

    override fun createInteractionEvent(npc: Npc) = FirstNpcClick(npc)

}