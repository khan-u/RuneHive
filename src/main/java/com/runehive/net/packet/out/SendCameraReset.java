package com.runehive.net.packet.out;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.OutgoingPacket;

/**
 * The {@code OutgoingPacket} resets the camera position for {@code Player}.
 * 
 * @author Daniel | Obey
 */
public class SendCameraReset extends OutgoingPacket {

	public SendCameraReset() {
		super(107, 0);
	}

	@Override
	public boolean encode(Player player) {
		return true;
	}
	
}
