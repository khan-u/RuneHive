package com.runehive.net.packet.in;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.GamePacket;
import com.runehive.net.packet.PacketListener;
import com.runehive.net.packet.PacketListenerMeta;
import org.jire.runehiveps.event.widget.InputStringEvent;

/**
 * The {@link GamePacket} responsible for reciving a string sent by the
 * client.
 * 
 * @author Michael | Chex
 */
@PacketListenerMeta(60)
public class InputStringPacketListener implements PacketListener {

	@Override
	public void handlePacket(Player player, GamePacket packet) {
		String inputText = packet.getRS2String();
		
		System.out.println("[DEBUG InputStringPacket] Received from " + player.getUsername() + ": '" + inputText + "'");
		
		if (inputText == null || inputText.isEmpty()) {
			System.out.println("[DEBUG InputStringPacket] Empty or null input, ignoring");
			return;
		}
		
		// Handle AI dialogue input
		if (player.enterInputListener.isPresent()) {
			System.out.println("[DEBUG InputStringPacket] Found enterInputListener, processing: " + inputText);
			player.enterInputListener.get().accept(inputText);
			player.enterInputListener = java.util.Optional.empty();
			return;
		}
		
		System.out.println("[DEBUG InputStringPacket] No enterInputListener, trying legacy long parse");
		
		// Legacy long input for other systems
		try {
			final long inputLong = Long.parseLong(inputText);
			player.getEvents().widget(player, new InputStringEvent(inputLong));
		} catch (NumberFormatException e) {
			System.out.println("[DEBUG InputStringPacket] Not a number: " + inputText);
		}
	}

}