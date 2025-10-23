package com.runehive.net.packet.in;

import com.runehive.net.packet.GamePacket;
import com.runehive.net.packet.PacketListenerMeta;
import com.runehive.net.packet.PacketListener;
import com.runehive.net.packet.ClientPackets;
import com.runehive.game.world.entity.mob.player.Player;

/**
 * The {@link GamePacket} responsible for closing interfaces.
 * 
 * @author Daniel
 */
@PacketListenerMeta(130)
public class CloseInterfacePacketListener implements PacketListener {

	@Override
	public void handlePacket(Player player, GamePacket packet) {

		switch (packet.getOpcode()) {

		case ClientPackets.CLOSE_WINDOW:
			player.interfaceManager.close(false);
			break;
		}
	}
}