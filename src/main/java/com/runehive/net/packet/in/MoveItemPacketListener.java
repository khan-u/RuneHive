package com.runehive.net.packet.in;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.codec.ByteModification;
import com.runehive.net.codec.ByteOrder;
import com.runehive.net.packet.ClientPackets;
import com.runehive.net.packet.GamePacket;
import com.runehive.net.packet.PacketListener;
import com.runehive.net.packet.PacketListenerMeta;
import org.jire.runehiveps.event.widget.MoveItemEvent;

@PacketListenerMeta(ClientPackets.MOVE_ITEM)
public class MoveItemPacketListener implements PacketListener {

    @Override
    public void handlePacket(Player player, GamePacket packet) {
        final int interfaceId = packet.readShort(ByteOrder.LE, ByteModification.ADD);
        final int inserting = packet.readByte(ByteModification.NEG);
        final int fromSlot = packet.readShort(ByteOrder.LE, ByteModification.ADD);
        final int toSlot = packet.readShort(ByteOrder.LE);

        player.idle = false;

        player.getEvents().widget(player, new MoveItemEvent(interfaceId, inserting, fromSlot, toSlot));
    }

}
