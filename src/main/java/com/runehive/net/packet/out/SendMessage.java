package com.runehive.net.packet.out;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.OutgoingPacket;
import com.runehive.net.packet.PacketType;
import com.runehive.util.MessageColor;

/**
 * The {@code OutgoingPacket} that sends a message to a {@code Player}s chatbox
 * in the client.
 *
 * @author Michael | Chex
 */
public class SendMessage extends OutgoingPacket {

	private final Object message;
	private final boolean filtered;

	public SendMessage(Object message) {
		this(message, MessageColor.BLACK);
	}

	public SendMessage(Object message, boolean filtered) {
		this(message, MessageColor.BLACK, filtered);
	}

	public SendMessage(Object message, MessageColor color) {
		this(message, color, false);
	}

	public SendMessage(Object message, MessageColor color, boolean filtered) {
		super(253, PacketType.VAR_BYTE);
		this.message = (color == MessageColor.BLACK ? "" : "<col=" + color.getColor() + ">") + message;
		this.filtered = filtered;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeString(String.valueOf(message));
		builder.writeByte(filtered ? 1 : 0);
		return true;
	}
}
