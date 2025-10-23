package com.runehive.net.packet.out;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.OutgoingPacket;

public class SendNpcDisplay extends OutgoingPacket {

	private final int npc;
	private final int size;
	
	public SendNpcDisplay(int npc, int size) {
		super(198, 0);
		this.npc = npc;
		this.size = size;
	}
	
	@Override
	public boolean encode(Player player) {
	return false;
		//Changes size of npc on client. gotta redo
//		builder.writeInt(npc).writeInt(size);
//		player.channel.ifPresent(s -> s.submit(builder));
	}
	
}
