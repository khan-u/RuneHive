package org.jire.runehiveps.event.widget

import com.runehive.content.clanchannel.channel.ClanChannel
import com.runehive.game.event.impl.CommandEvent
import com.runehive.game.plugin.PluginManager
import com.runehive.game.world.entity.mob.player.Player
import com.runehive.game.world.entity.mob.player.command.CommandParser
import com.runehive.util.Utility

/**
 * @author Jire
 */
class CommandEvent(val input: String) : WidgetEvent {

    override fun handle(player: Player) {
        val parser = CommandParser.split(input, " ")

        if (parser.command.startsWith("/")) {
            if (player.punishment.isMuted) {
                player.message("You can not send clan messages while muted!")
                return
            }
            player.forClan { channel: ClanChannel ->
                val copy = CommandParser.split(input, "/")
                if (copy.hasNext()) {
                    val line = copy.nextLine()
                    channel.chat(player.name, Utility.capitalizeSentence(line))
                }
            }
            return
        }

        PluginManager.getDataBus().publish(player, CommandEvent(parser))
    }

}