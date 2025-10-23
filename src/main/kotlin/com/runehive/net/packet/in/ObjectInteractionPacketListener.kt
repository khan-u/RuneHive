package com.runehive.net.packet.`in`

import com.runehive.content.event.impl.FirstObjectClick
import com.runehive.content.event.impl.SecondObjectClick
import com.runehive.content.event.impl.ThirdObjectClick
import com.runehive.game.world.entity.mob.data.PacketType
import com.runehive.game.world.entity.mob.player.Player
import com.runehive.net.codec.ByteModification
import com.runehive.net.codec.ByteOrder
import com.runehive.net.packet.ClientPackets
import com.runehive.net.packet.GamePacket
import com.runehive.net.packet.PacketListener
import com.runehive.net.packet.PacketListenerMeta
import org.jire.runehiveps.event.`object`.ObjectOptionEvent

/**
 * The `GamePacket` responsible for clicking various options of an in-game
 * object.
 *
 * @author Daniel | Obey
 * @author Jire
 */
@PacketListenerMeta(
    ClientPackets.FIRST_CLICK_OBJECT,
    ClientPackets.SECOND_CLICK_OBJECT,
    ClientPackets.THIRD_CLICK_OBJECT
)
class ObjectInteractionPacketListener : PacketListener {

    override fun handlePacket(player: Player, packet: GamePacket) {
        if (player.locking.locked(PacketType.CLICK_OBJECT)) return

        val event: ObjectOptionEvent = when (packet.opcode) {
            ClientPackets.FIRST_CLICK_OBJECT -> {
                val x = packet.readShort(ByteOrder.LE, ByteModification.ADD)
                val id = packet.readShort(false)
                val y = packet.readShort(false, ByteModification.ADD)
                ObjectOptionEvent(1, id, x, y) { FirstObjectClick(it) }
            }

            ClientPackets.SECOND_CLICK_OBJECT -> {
                val id = packet.readShort(ByteOrder.LE, ByteModification.ADD)
                val y = packet.readShort(ByteOrder.LE)
                val x = packet.readShort(false, ByteModification.ADD)
                ObjectOptionEvent(2, id, x, y) { SecondObjectClick(it) }
            }

            ClientPackets.THIRD_CLICK_OBJECT -> {
                val x = packet.readShort(ByteOrder.LE)
                val y = packet.readShort(false)
                val id = packet.readShort(false, ByteOrder.LE, ByteModification.ADD)
                ObjectOptionEvent(3, id, x, y) { ThirdObjectClick(it) }
            }

            else -> return
        }
        player.events.interact(player, event)
    }

}