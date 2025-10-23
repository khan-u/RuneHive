package com.runehive.net.packet.in;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.ClientPackets;
import com.runehive.net.packet.GamePacket;
import com.runehive.net.packet.PacketListener;
import com.runehive.net.packet.PacketListenerMeta;
import org.jire.runehiveps.event.widget.PlayerRelationEvent;

/**
 * The {@link GamePacket}'s responsible for player communication.
 *
 * @author Daniel | Obey
 */
@PacketListenerMeta({ClientPackets.ADD_FRIEND, ClientPackets.PRIVATE_MESSAGE, ClientPackets.REMOVE_FRIEND, ClientPackets.REMOVE_IGNORE, ClientPackets.ADD_IGNORE})
public final class PlayerRelationPacketListener implements PacketListener {

    @Override
    public void handlePacket(Player player, GamePacket packet) {
        final int opcode = packet.getOpcode();
        final long username = packet.readLong();
        player.getEvents().widget(player,
                new PlayerRelationEvent(opcode, username,
                        opcode == ClientPackets.PRIVATE_MESSAGE
                                ? packet.readBytes(packet.getSize() - Long.BYTES)
                                : null));
    }

}
