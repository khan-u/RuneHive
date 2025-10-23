package com.runehive.game.action.impl;

import com.runehive.game.action.Action;
import com.runehive.game.action.policy.WalkablePolicy;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.position.Position;

public abstract class InspectAction extends Action<Player> {

	/**
	 * The position.
	 */
	private Position position;

	/**
	 * Constructor.
	 * 
	 * @param player
	 * @param position
	 */
	public InspectAction(Player player, Position position) {
		super(player, 1, true);
		this.position = position;
	}

	@Override
	public boolean prioritized() {
		return false;
	}

	@Override
	public WalkablePolicy getWalkablePolicy() {
		return WalkablePolicy.NON_WALKABLE;
	}

	@Override
	public void execute() {
		if (getDelay() == 0) {
			setDelay(getInspectDelay());
			init();
			if (this.isRunning()) {
				getMob().face(position);
			}
		} else {
			giveRewards(getMob());
			cancel();
		}
	}
	
	/**
	 * Initialization method.
	 */
	public abstract void init();

	/**
	 * Inspection time consumption.
	 * @return
	 */
	public abstract int getInspectDelay();

	/**
	 * Rewards to give the player.
	 * @param player
	 */
	public abstract void giveRewards(Player player);

}