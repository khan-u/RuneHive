package com.runehive.net.packet.out;

import com.runehive.net.codec.ByteModification;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.OutgoingPacket;

public class SendMapRegion extends OutgoingPacket {

	public SendMapRegion() {
		super(73, 4);
	}

	@Override
	public boolean encode(Player player) {
		player.lastPosition = player.getPosition().copy();
		
		// 317-style region in CHUNKS (8x8)
		int regionX = (player.getPosition().getX() >> 3);
		int regionY = (player.getPosition().getY() >> 3);
		
		// The client's scene base (chunk coords) is region - 6
		int sceneBaseChunkX = regionX - 6;
		int sceneBaseChunkY = regionY - 6;
		
		// Persist the exact base the client will use
		player.setSceneBaseChunks(sceneBaseChunkX, sceneBaseChunkY);
		
		System.out.printf("[MapRegion] sceneBaseChunks=(%d,%d) tiles=(%d,%d)%n",
			sceneBaseChunkX, sceneBaseChunkY,
			sceneBaseChunkX * 8, sceneBaseChunkY * 8
		);
		
		builder.writeShort(player.getPosition().getChunkX() + 6, ByteModification.ADD)
		.writeShort(player.getPosition().getChunkY() + 6);
		return true;
	}

}
