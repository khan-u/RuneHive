package com.runehive.net.packet.out;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.OutgoingPacket;

public class SendLogout extends OutgoingPacket {

	public SendLogout() {
		super(109, 0);
	}

	@Override
	public boolean encode(Player player) {
		return true;
	}

}
