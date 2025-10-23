package com.runehive.net.packet.out;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.OutgoingPacket;

public class SendWalkableInterface extends OutgoingPacket {

	private final int id;

	public SendWalkableInterface(int id) {
		super(208, 2);
		this.id = id;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeShort(id);
		return true;
	}

}
