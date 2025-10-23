package org.jire.runehiveps.event.player

import com.runehive.game.event.impl.ItemOnPlayerEvent
import com.runehive.game.plugin.PluginManager
import com.runehive.game.world.World
import com.runehive.game.world.entity.mob.player.Player
import com.runehive.net.packet.out.SendMessage
import org.jire.runehiveps.event.Event
import kotlin.jvm.optionals.getOrNull

/**
 * @author Jire
 */
class ItemOnPlayerEvent(
    val interfaceId: Int,
    val item: Int,
    val itemSlot: Int,
    val slot: Int
) : Event {

    override fun handle(player: Player) {
        val used = player.inventory[itemSlot] ?: return
        if (!used.matchesId(item)) return

        val other = World.getPlayerBySlot(slot).getOrNull() ?: return
        
        player.walkTo(other) {
            player.face(other.position)
            if (!PluginManager.getDataBus().publish(player, ItemOnPlayerEvent(other, used, itemSlot))) {
                player.send(SendMessage("Nothing interesting happens."))
            }
        }
    }

}