package com.runehive.net.packet.out;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.codec.ByteModification;
import com.runehive.net.packet.OutgoingPacket;
import com.runehive.net.packet.PacketType;

/**
 * The {@code OutgoingPacket} that sends a string to a {@code Player}s itemcontainer
 * in the client.
 * 
 * @author Daniel | Obey
 */
public class SendString extends OutgoingPacket {

	private final Object string;
	private final int id;
	private final boolean override;

	public SendString(Object string, int id, boolean override) {
		super(126, PacketType.VAR_SHORT);
		this.string = string;
		this.id = id;
		this.override = override;
	}

	public SendString(Object string, int id) {
		this(string, id, false);
	}

	@Override
	public boolean encode(Player player) {
		if (!override) {
			if (id != 0 && !player.playerAssistant.checkSendString(String.valueOf(string), id)) {
				return false;
			}
		}
		builder.writeString(String.valueOf(string)).writeInt(id);
		return true;
	}

}
