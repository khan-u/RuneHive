package com.runehive.net.packet.out;

import com.runehive.net.codec.ByteOrder;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.OutgoingPacket;

public class SendChatBoxInterface extends OutgoingPacket {

	private final int interfaceId;

	public SendChatBoxInterface(int interfaceId) {
		super(164, 2);
		this.interfaceId = interfaceId;
	}

	@Override
	public boolean encode(Player player) {
		player.interfaceManager.setDialogue(1);
		builder.writeShort(interfaceId, ByteOrder.LE);
		return true;
	}
}
