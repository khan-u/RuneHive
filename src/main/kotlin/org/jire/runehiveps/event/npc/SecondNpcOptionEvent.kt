package org.jire.runehiveps.event.npc

import com.runehive.content.event.impl.SecondNpcClick
import com.runehive.game.world.entity.mob.npc.Npc

/**
 * @author Jire
 */
class SecondNpcOptionEvent(slot: Int) : NpcOptionEvent(slot, 1) {

    override fun createInteractionEvent(npc: Npc) = SecondNpcClick(npc)

}