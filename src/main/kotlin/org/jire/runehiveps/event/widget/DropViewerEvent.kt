package org.jire.runehiveps.event.widget

import com.runehive.content.DropDisplay
import com.runehive.content.DropDisplay.DropType
import com.runehive.game.world.entity.mob.npc.drop.NpcDropManager
import com.runehive.game.world.entity.mob.player.Player

/**
 * @author Jire
 */
class DropViewerEvent(val context: String) : WidgetEvent {

    override fun handle(player: Player) {
        DropDisplay.search(player, context, DropType.NPC)

        if (!player.attributes.has("DROP_DISPLAY_KEY")) return
        val key = player.attributes.get("DROP_DISPLAY_KEY", List::class.java)
        if (key == null || key.isEmpty()) return

        DropDisplay.display(player, NpcDropManager.NPC_DROPS[key[0]])
        player.interfaceManager.open(54500)
    }

}