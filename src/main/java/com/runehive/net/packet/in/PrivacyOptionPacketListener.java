package com.runehive.net.packet.in;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.ClientPackets;
import com.runehive.net.packet.GamePacket;
import com.runehive.net.packet.PacketListener;
import com.runehive.net.packet.PacketListenerMeta;
import org.jire.runehiveps.event.widget.PrivacyOptionEvent;

@PacketListenerMeta({ClientPackets.PRIVACY_OPTIONS})
public final class PrivacyOptionPacketListener implements PacketListener {

    @Override
    public void handlePacket(Player player, GamePacket packet) {
        final int publicMode = packet.readByte();
        final int privateMode = packet.readByte();
        final int tradeMode = packet.readByte();
        final int clanMode = packet.readByte();

        player.getEvents().widget(player,
                new PrivacyOptionEvent(
                        publicMode,
                        privateMode,
                        tradeMode,
                        clanMode));
    }

}
