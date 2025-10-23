package com.runehive.net.packet.`in`

import com.runehive.content.ProfileViewer
import com.runehive.game.world.entity.mob.player.Player
import com.runehive.game.world.entity.mob.player.exchange.duel.StakeSession
import com.runehive.game.world.entity.mob.player.exchange.trade.TradeSession
import com.runehive.net.codec.ByteModification
import com.runehive.net.codec.ByteOrder
import com.runehive.net.packet.ClientPackets
import com.runehive.net.packet.GamePacket
import com.runehive.net.packet.PacketListener
import com.runehive.net.packet.PacketListenerMeta
import org.jire.runehiveps.event.player.MagicOnPlayerEvent
import org.jire.runehiveps.event.player.PlayerEvent.Companion.walkTo

/**
 * The [GamePacket]s responsible interacting with other players.
 *
 * @author Daniel | Obey
 * @author Jire
 */
@PacketListenerMeta(
    ClientPackets.TRADE_REQUEST,
    ClientPackets.TRADE_ANSWER,
    ClientPackets.CHALLENGE_PLAYER,
    ClientPackets.FOLLOW_PLAYER,
    ClientPackets.MAGIC_ON_PLAYER,
    ClientPackets.ATTACK_PLAYER,
    ClientPackets.GAMBLE_PLAYER
)
class PlayerOptionPacketListener : PacketListener {

    override fun handlePacket(player: Player, packet: GamePacket) {
        val event = when (packet.opcode) {
            ClientPackets.GAMBLE_PLAYER -> walkTo(packet.readShort()) {
                player.gambling.sendRequest(player, it)
            }

            128 -> walkTo(packet.readShort()) {
                player.exchangeSession.request(StakeSession(player, it))
            }

            153 -> walkTo(packet.readShort(ByteOrder.LE)) {
                player.combat.attack(it)
            }

            73 -> walkTo(packet.readShort(ByteOrder.LE)) {
                player.follow(it)
            }

            139 -> walkTo(packet.readShort(ByteOrder.LE)) {
                player.exchangeSession.request(TradeSession(player, it))
            }

            39 -> walkTo(packet.readShort(ByteOrder.LE)) {
                ProfileViewer.open(player, it)
            }

            ClientPackets.MAGIC_ON_PLAYER -> MagicOnPlayerEvent(
                packet.readShort(ByteModification.ADD),
                packet.readShort(ByteOrder.LE)
            )

            else -> return
        }

        player.events.interact(player, event)
    }

}
