package com.runehive.net.packet.in;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.ClientPackets;
import com.runehive.net.packet.GamePacket;
import com.runehive.net.packet.PacketListener;
import com.runehive.net.packet.PacketListenerMeta;
import org.jire.runehiveps.event.widget.DropViewerEvent;

@PacketListenerMeta({ClientPackets.NPC_DROP_VIEWER})
public class DropViewerListener implements PacketListener {

    @Override
    public void handlePacket(Player player, GamePacket packet) {
        final String context = packet.getRS2String();
        if (context == null || context.isEmpty() || context.equalsIgnoreCase("null")) {
            return;
        }

        player.getEvents().widget(player, new DropViewerEvent(context));
    }
}
