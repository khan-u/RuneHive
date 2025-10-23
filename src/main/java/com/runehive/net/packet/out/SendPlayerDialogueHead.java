package com.runehive.net.packet.out;

import com.runehive.net.codec.ByteModification;
import com.runehive.net.codec.ByteOrder;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.OutgoingPacket;

public class SendPlayerDialogueHead extends OutgoingPacket {

	private final int interfaceId;

	public SendPlayerDialogueHead(int interfaceId) {
		super(185, 2);
		this.interfaceId = interfaceId;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeShort(interfaceId, ByteModification.ADD, ByteOrder.LE);
		return true;
	}

}
