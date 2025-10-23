package org.jire.runehiveps.event.widget

import com.runehive.game.world.World
import com.runehive.game.world.entity.mob.player.Player
import com.runehive.game.world.entity.mob.player.relations.PrivateChatMessage
import com.runehive.net.packet.ClientPackets
import com.runehive.util.ChatCodec
import com.runehive.util.Utility
import kotlin.jvm.optionals.getOrNull

/**
 * @author Jire
 */
class PlayerRelationEvent(
    val opcode: Int,
    val username: Long,
    val input: ByteArray? = null
) : WidgetEvent {

    override fun handle(player: Player) {
        when (opcode) {
            ClientPackets.ADD_FRIEND -> player.relations.addFriend(username)
            ClientPackets.REMOVE_FRIEND -> player.relations.deleteFriend(username)
            ClientPackets.ADD_IGNORE -> player.relations.addIgnore(username)
            ClientPackets.REMOVE_IGNORE -> player.relations.deleteIgnore(username)
            ClientPackets.PRIVATE_MESSAGE -> {
                val other = World.search(
                    Utility.formatText(
                        Utility.longToString(username)
                    ).replace('_', ' ')
                ).getOrNull() ?: return

                val decoded = ChatCodec.decode(input)
                val compressed = ChatCodec.encode(decoded)

                player.relations.message(other, PrivateChatMessage(decoded, compressed))
            }
        }
    }

}