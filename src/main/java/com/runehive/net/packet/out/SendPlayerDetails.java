package com.runehive.net.packet.out;

import com.runehive.net.codec.ByteOrder;
import com.runehive.net.codec.ByteModification;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.OutgoingPacket;

public class SendPlayerDetails extends OutgoingPacket {

	public SendPlayerDetails() {
		super(249, 3);
	}

	@Override
	public boolean encode(Player player) {
		builder.writeByte(1, ByteModification.ADD)
		.writeShort(player.getIndex(), ByteModification.ADD, ByteOrder.LE);
		return true;
	}

}
