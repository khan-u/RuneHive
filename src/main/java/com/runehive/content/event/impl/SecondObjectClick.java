package com.runehive.content.event.impl;

import com.runehive.content.event.InteractionEvent;
import com.runehive.game.world.object.GameObject;

public class SecondObjectClick extends ObjectInteractionEvent {

	public SecondObjectClick(GameObject object) {
		super(InteractionEvent.InteractionType.SECOND_CLICK_OBJECT, object, 1);
	}

}