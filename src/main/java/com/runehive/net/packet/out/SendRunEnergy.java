package com.runehive.net.packet.out;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.OutgoingPacket;

public final class SendRunEnergy extends OutgoingPacket {

	public SendRunEnergy() {
		super(110, 1);
	}

	@Override
	public boolean encode(Player player) {
		builder.writeByte(player.runEnergy);
		return true;
	}

}
