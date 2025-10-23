package com.runehive.net.packet.out;
import com.runehive.net.packet.OutgoingPacket;
import com.runehive.net.packet.PacketType;
import com.runehive.net.codec.ByteModification;
import com.runehive.game.world.entity.mob.player.Player;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Sends a chatbox input prompt that accepts full text with spaces.
 * This creates an input field in the chatbox where players can type messages.
 * Based on 317 protocol inputDialogState system - packet triggers client-side
 * text input mode that sends back typed string via packet 60.
 * 
 * @author Daniel
 * @author Michael  
 * @author AI Port (317 inputDialogState pattern)
 */
public class SendInputMessage extends OutgoingPacket {

	private final Consumer<String> action;
	private final String inputMessage;
	private final int inputLength;

	public SendInputMessage(String message, int length, Consumer<String> action) {
		super(187, PacketType.VAR_BYTE);
		this.action = action;
		this.inputMessage = message;
		this.inputLength = length;
	}

	@Override
	public boolean encode(Player player) {
		player.enterInputListener = Optional.of(action);
		// Packet 187 VAR_BYTE format: writes prompt string
		// Client should display chatbox input interface and send back via packet 60
		builder.writeString(inputMessage);
		return true;
	}

}
