package org.jire.runehiveps.event.npc

import com.runehive.game.world.World
import com.runehive.game.world.entity.mob.data.PacketType
import com.runehive.game.world.entity.mob.npc.Npc
import com.runehive.game.world.entity.mob.player.Player
import kotlin.jvm.optionals.getOrNull

/**
 * @author Jire
 */
interface NpcClickEvent : NpcEvent {

    val slot: Int

    override fun canHandle(player: Player) = super.canHandle(player)
            && !player.locking.locked(PacketType.CLICK_NPC)

    override fun handle(player: Player) {
        val npc = World.getNpcBySlot(slot).getOrNull() ?: return
        if (!npc.isValid) return

        val position = npc.position
        val region = World.getRegions().getRegion(position)
        if (!region.containsNpc(position.height, npc)) return

        handleNpc(player, npc)
    }

    fun handleNpc(player: Player, npc: Npc) {}

}