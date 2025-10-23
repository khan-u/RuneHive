package com.runehive.net.packet.out;

import com.runehive.net.packet.OutgoingPacket;
import com.runehive.net.packet.PacketType;
import com.runehive.game.world.entity.mob.player.Player;

/**
 * The {@code OutgoingPacket} that opens a URL from client.
 * 
 * @author Daniel | Obey 
 */
public class SendURL extends OutgoingPacket {

	private final String link;

	public SendURL(String link) {
		super(138, PacketType.VAR_BYTE);
		this.link = link;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeString(link);
		return true;
	}

}
