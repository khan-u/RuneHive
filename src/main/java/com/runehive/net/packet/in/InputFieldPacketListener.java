package com.runehive.net.packet.in;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.GamePacket;
import com.runehive.net.packet.PacketListener;
import com.runehive.net.packet.PacketListenerMeta;
import org.jire.runehiveps.event.widget.InputFieldEvent;

@PacketListenerMeta(142)
public class InputFieldPacketListener implements PacketListener {

    @Override
    public void handlePacket(Player player, GamePacket packet) {
        final int component = packet.readInt();
        final String context = packet.getRS2String();

        player.getEvents().widget(player, new InputFieldEvent(component, context));
    }
}
