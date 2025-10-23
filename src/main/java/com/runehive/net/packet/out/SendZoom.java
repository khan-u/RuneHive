package com.runehive.net.packet.out;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.OutgoingPacket;

public final class SendZoom extends OutgoingPacket {

	private final int zoom;

	public SendZoom(int zoom) {
		super(139, 2);
		this.zoom = zoom;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeShort(zoom);
		return true;
	}
}
