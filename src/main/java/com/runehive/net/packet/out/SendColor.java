package com.runehive.net.packet.out;

import com.runehive.net.codec.ByteOrder;
import com.runehive.net.codec.ByteModification;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.OutgoingPacket;

/**
 * The {@code OutgoingPacket} that sends a color to a string
 * in the client.
 * 
 * @author Daniel | Obey 
 */
public class SendColor extends OutgoingPacket {

	private final int id;
	private final int color;

	public SendColor(int id, int color) {
		super(122, 6);
		this.id = id;
		this.color = color;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeShort(id, ByteModification.ADD, ByteOrder.LE)
		.writeInt(color);
		return true;
	}

}
