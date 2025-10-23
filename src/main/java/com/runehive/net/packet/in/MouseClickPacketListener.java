package com.runehive.net.packet.in;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.GamePacket;
import com.runehive.net.packet.PacketListenerMeta;
import com.runehive.net.packet.PacketListener;

@PacketListenerMeta({241})
public class MouseClickPacketListener implements PacketListener {

    @Override
    public void handlePacket(Player player, GamePacket packet) {

    }

}
