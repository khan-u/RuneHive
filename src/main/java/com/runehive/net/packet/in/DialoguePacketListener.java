package com.runehive.net.packet.in;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.GamePacket;
import com.runehive.net.packet.PacketListener;
import com.runehive.net.packet.PacketListenerMeta;
import org.jire.runehiveps.event.widget.DialogueEvent;

/**
 * The {@link GamePacket} responsible for dialogues.
 * 
 * @author Daniel | Obey
 */
@PacketListenerMeta(40)
public class DialoguePacketListener implements PacketListener {

	@Override
	public void handlePacket(Player player, GamePacket packet) {
		player.getEvents().widget(player, DialogueEvent.INSTANCE);
	}

}
