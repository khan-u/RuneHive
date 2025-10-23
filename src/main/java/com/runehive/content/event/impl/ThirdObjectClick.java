package com.runehive.content.event.impl;

import com.runehive.game.world.object.GameObject;

public class ThirdObjectClick extends ObjectInteractionEvent {

	public ThirdObjectClick(GameObject object) {
		super(InteractionType.THIRD_CLICK_OBJECT, object, 2);
	}

}