package com.runehive.net.packet.out;

import com.runehive.net.codec.ByteOrder;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.OutgoingPacket;

public class SendMoveComponent extends OutgoingPacket {

	private final int id;
	private final int x, y;

	public SendMoveComponent(int x, int y, int id) {
		super(70, 6);
		this.x = x;
		this.y = y;
		this.id = id;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeShort(x)
		.writeShort(y, ByteOrder.LE)
		.writeShort(id, ByteOrder.LE);
		return true;
	}

}
