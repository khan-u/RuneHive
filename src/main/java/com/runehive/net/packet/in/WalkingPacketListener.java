
package com.runehive.net.packet.in;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.codec.ByteModification;
import com.runehive.net.codec.ByteOrder;
import com.runehive.net.packet.ClientPackets;
import com.runehive.net.packet.GamePacket;
import com.runehive.net.packet.PacketListener;
import com.runehive.net.packet.PacketListenerMeta;
import org.jire.runehiveps.event.player.WalkEvent;

/**
 * A packet which handles walking requests.
 *
 * @author Graham Edgecombe
 */
@PacketListenerMeta({ClientPackets.WALK_ON_COMMAND, ClientPackets.REGULAR_WALK, ClientPackets.MAP_WALK})
public class WalkingPacketListener implements PacketListener {

    @Override
    public void handlePacket(Player player, GamePacket packet) {
        final int targetX = packet.readShort(ByteOrder.LE);
        final int targetY = packet.readShort(ByteOrder.LE, ByteModification.ADD);
        final boolean runQueue = packet.readByte(ByteModification.NEG) == 1;

        player.getEvents().interact(player, new WalkEvent(targetX, targetY, runQueue));
    }

}