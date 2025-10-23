package com.runehive.net.packet.out;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.OutgoingPacket;

public class SendAddIgnore extends OutgoingPacket {

	private final long usernameLong;

	public SendAddIgnore(long usernameLong) {
		super(214, 8);
		this.usernameLong = usernameLong;
	}

	@Override
	protected boolean encode(Player player) {
		builder.writeLong(usernameLong);
		return true;
	}

}
