package com.runehive.net.packet.in;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.GamePacket;
import com.runehive.net.packet.PacketListener;
import com.runehive.net.packet.PacketListenerMeta;

/**
 * The {@link GamePacket} responsible logging out a player after a certain
 * amount of time.
 * 
 * @author Daniel
 */
@PacketListenerMeta(202)
public class IdleLogoutPacketListener implements PacketListener {

    @Override
    public void handlePacket(Player player, GamePacket packet) {
        player.idle = true;
    }

}