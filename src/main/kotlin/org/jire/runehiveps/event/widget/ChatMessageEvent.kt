package org.jire.runehiveps.event.widget

import com.runehive.game.event.impl.log.ChatLogEvent
import com.runehive.game.world.World
import com.runehive.game.world.entity.mob.player.Player
import com.runehive.game.world.entity.mob.player.relations.ChatColor
import com.runehive.game.world.entity.mob.player.relations.ChatEffect
import com.runehive.game.world.entity.mob.player.relations.ChatMessage
import com.runehive.util.ChatCodec
import java.util.*

/**
 * @author Jire
 */
class ChatMessageEvent(
    val effect: Int,
    val color: Int,
    val size: Int,
    val bytes: ByteArray
) : WidgetEvent {

    override fun handle(player: Player) {
        val decoded = ChatCodec.decode(bytes)

        player.chat(ChatMessage.create(decoded, ChatColor.values[color], ChatEffect.values[effect]))
        World.getDataBus().publish(ChatLogEvent(player, decoded))

        println("User: " + player.username.uppercase(Locale.getDefault()) + " said: " + decoded)
    }

}