package org.jire.runehiveps.event.item

import com.runehive.game.world.entity.mob.player.Player
import com.runehive.net.packet.out.SendMessage
import org.jire.runehiveps.defs.ItemDefLoader
import org.jire.runehiveps.event.Event

/**
 * @author Jire
 */
class ItemExamineEvent(val itemId: Int) : Event {

    override fun handle(player: Player) {
        val itemDef = ItemDefLoader.map[itemId] ?: return
        val examine = itemDef.examine
        if ("null" != examine) {
            player.send(SendMessage(examine))
        }
    }

}