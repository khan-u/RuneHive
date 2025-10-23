package com.runehive.net.packet.out;

import com.runehive.net.packet.OutgoingPacket;
import com.runehive.net.packet.PacketType;
import com.runehive.net.codec.ByteModification;
import com.runehive.game.world.entity.mob.player.Player;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Sends an input dialogue.
 * 
 * @author Daniel
 * @author Michael
 */
public class SendInputAmount extends OutgoingPacket {

	private final Consumer<String> action;
	private final String inputMessage;
	private final int inputLength;

	public SendInputAmount(Consumer<String> action) {
		this("Enter an amount:", 10, action);
	}

	public SendInputAmount(String message, int length, Consumer<String> action) {
		super(27, PacketType.VAR_SHORT);
		this.action = action;
		this.inputMessage = message;
		this.inputLength = length;
	}

	@Override
	public boolean encode(Player player) {
		player.enterInputListener = Optional.of(action);
		builder.writeString(inputMessage)
		.writeShort(inputLength, ByteModification.ADD);
		return true;
	}

}
