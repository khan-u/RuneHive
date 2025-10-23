package com.runehive.net.packet.in;

import com.runehive.net.packet.GamePacket;
import com.runehive.net.packet.PacketListenerMeta;
import com.runehive.net.packet.PacketListener;
import com.runehive.net.packet.out.SendMessage;
import com.runehive.net.codec.ByteOrder;
import com.runehive.game.world.entity.mob.UpdateFlag;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.entity.mob.player.PlayerRight;
import com.runehive.content.tittle.PlayerTitle;
import com.runehive.util.MessageColor;

@PacketListenerMeta(187)
public class ColorPacketListener implements PacketListener {

	@Override
	public void handlePacket(Player player, GamePacket packet) {
		int identification = packet.readShort(ByteOrder.LE);
		int value = packet.readInt();
		
		if (player.right.equals(PlayerRight.OWNER)) {
			player.send(new SendMessage("[ColorPacket] - Identification: " + identification + " Value: " + value, MessageColor.DEVELOPER));
		}

		switch (identification) {
		
		case 0:
			player.playerTitle = PlayerTitle.create(player.playerTitle.getTitle(), value);
			player.updateFlags.add(UpdateFlag.APPEARANCE);
			break;
			
		case 1:
			//yell
			break;
		}
	}
}
