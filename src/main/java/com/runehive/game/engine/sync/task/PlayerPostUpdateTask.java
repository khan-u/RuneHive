package com.runehive.game.engine.sync.task;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.session.GameSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class PlayerPostUpdateTask extends SynchronizationTask {

	private static final Logger logger = LogManager.getLogger(PlayerPostUpdateTask.class);

	private final Player player;

	public PlayerPostUpdateTask(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		try {
			player.viewport.calculateViewingDistance();
			player.updateFlags.clear();
			player.resetAnimation();
			player.resetGraphic();
			player.clearTeleportTarget();
			player.positionChange = false;
			player.regionChange = false;
			player.teleportRegion = false;
			player.facePosition = null;
			player.getEvents().reset();
			player.getSession().ifPresent(GameSession::processServerPacketQueue);
		} catch (Exception ex) {
			logger.error(String.format("Error in %s", PlayerPostUpdateTask.class.getSimpleName()), ex);
		}
	}

}
