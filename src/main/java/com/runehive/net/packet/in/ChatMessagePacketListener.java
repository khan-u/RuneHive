package com.runehive.net.packet.in;

import com.runehive.game.world.entity.mob.data.PacketType;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.entity.mob.player.relations.ChatColor;
import com.runehive.game.world.entity.mob.player.relations.ChatEffect;
import com.runehive.net.codec.ByteModification;
import com.runehive.net.packet.ClientPackets;
import com.runehive.net.packet.GamePacket;
import com.runehive.net.packet.PacketListener;
import com.runehive.net.packet.PacketListenerMeta;
import com.runehive.net.packet.out.SendMessage;
import org.jire.runehiveps.event.widget.ChatMessageEvent;


/**
 * The {@code GamePacket} responsible for chat messages.
 * 
 * @author Daniel
 */
@PacketListenerMeta(ClientPackets.CHAT)
public class ChatMessagePacketListener implements PacketListener {

	@Override
	public void handlePacket(Player player, GamePacket packet) {
		final int effect = packet.readByte(false, ByteModification.SUB);
		final int color = packet.readByte(false, ByteModification.SUB);
		final int size = packet.getSize() - 2;

		if (effect < 0 || effect >= ChatEffect.values.length || color < 0 || color >= ChatColor.values.length || size <= 0) {
			return;
		}

		if (player.locking.locked(PacketType.CHAT)) {
			return;
		}

		player.idle = false;

		if (player.punishment.isMuted()) {
			player.send(new SendMessage("You are currently muted and can not talk!"));
			return;
		}

		final byte[] bytes = packet.readBytesReverse(size, ByteModification.ADD);
		player.getEvents().widget(player, new ChatMessageEvent(effect, color, size, bytes));
	}

}