package com.runehive.net.packet.out;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.codec.ByteModification;
import com.runehive.net.codec.ByteOrder;
import com.runehive.net.packet.OutgoingPacket;
import com.runehive.net.packet.PacketType;
import com.runehive.util.MessageColor;

public class SendInterfaceWidget extends OutgoingPacket {

	private final int interfaceID;
	private final int modelID;

	public SendInterfaceWidget(int interfaceID, int modelID) {
		super(8, PacketType.EMPTY);
		this.interfaceID = interfaceID;
		this.modelID = modelID;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeShort(interfaceID, ByteModification.ADD, ByteOrder.LE);
		builder.writeShort(modelID);
		return true;
	}
}
