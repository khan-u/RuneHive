package com.runehive.net.packet.out;

import com.runehive.net.codec.ByteModification;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.object.GameObject;
import com.runehive.net.packet.OutgoingPacket;

public class SendRemoveObject extends OutgoingPacket {

	private final GameObject object;

	public SendRemoveObject(GameObject object) {
		super(101, 2);
		this.object = object;
	}

	@Override
	public boolean encode(Player player) {
		player.send(new SendCoordinate(object.getPosition()));
		builder.writeByte(object.getObjectType().getId() << 2 | (object.getDirection().getId() & 3), ByteModification.NEG)
		.writeByte(0);
		return true;
	}
}