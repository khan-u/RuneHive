package com.runehive.net.packet.out;

import com.runehive.net.codec.ByteModification;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.OutgoingPacket;

public class SendSpecialEnabled extends OutgoingPacket {
	
	private final int id;

	public SendSpecialEnabled(int id) {
		super(183, 1);
		this.id = id;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeByte(id, ByteModification.NEG);
		return true;
	}
}

