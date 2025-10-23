package com.runehive.net.packet.in;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.entity.mob.player.relations.ChatMessage;
import com.runehive.net.packet.ClientPackets;
import com.runehive.net.packet.GamePacket;
import com.runehive.net.packet.PacketListener;
import com.runehive.net.packet.PacketListenerMeta;

/**
 * The {@code GamePacket} responsible for handling user commands send from the
 * client.
 *
 * @author Michael | Chex
 */
@PacketListenerMeta(ClientPackets.PLAYER_COMMAND)
public final class CommandPacketListener implements PacketListener {

    @Override
    public void handlePacket(Player player, GamePacket packet) {
        final String input = packet.getRS2String().trim().toLowerCase();
        if (input.isEmpty() || input.length() > ChatMessage.CHARACTER_LIMIT) {
            return;
        }

        player.getEvents().widget(player, new org.jire.runehiveps.event.widget.CommandEvent(input));
    }

}
