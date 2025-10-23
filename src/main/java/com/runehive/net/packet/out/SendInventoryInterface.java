package com.runehive.net.packet.out;

import com.runehive.net.codec.ByteModification;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.OutgoingPacket;

/**
 * The {@code OutgoingPacket} that opens the inventory with itemcontainer.
 * 
 * @author Daniel | Obey 
 */
public class SendInventoryInterface extends OutgoingPacket {

	private final int open;
	private final int overlay;

	public SendInventoryInterface(int open, int overlay) {
		super(248, 6);
		this.open = open;
		this.overlay = overlay;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeInt(open)
		.writeShort(overlay);
		return true;
	}

}
