package com.runehive.net.packet.out;

import com.runehive.net.codec.ByteOrder;
import com.runehive.net.codec.ByteModification;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.OutgoingPacket;

public class SendNpcHead extends OutgoingPacket {

	private final int npcId;
	private final int interfaceId;

	public SendNpcHead(int npcId, int interfaceId) {
		super(75, 4);
		this.npcId = npcId;
		this.interfaceId = interfaceId;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeShort(npcId, ByteModification.ADD, ByteOrder.LE)
		.writeShort(interfaceId, ByteModification.ADD, ByteOrder.LE);
		return true;
	}

}
