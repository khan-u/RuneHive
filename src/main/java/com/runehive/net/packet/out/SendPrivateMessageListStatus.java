package com.runehive.net.packet.out;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.entity.mob.player.relations.PrivateMessageListStatus;
import com.runehive.net.packet.OutgoingPacket;

public class SendPrivateMessageListStatus extends OutgoingPacket {

	private final PrivateMessageListStatus status;

	public SendPrivateMessageListStatus(PrivateMessageListStatus status) {
		super(221, 1);
		this.status = status;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeByte(status.ordinal());
		return true;
	}

}