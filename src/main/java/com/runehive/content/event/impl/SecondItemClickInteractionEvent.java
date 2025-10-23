package com.runehive.content.event.impl;

import com.runehive.content.event.InteractionEvent;
import com.runehive.game.world.items.Item;

public class SecondItemClickInteractionEvent extends ItemInteractionEvent {

	public SecondItemClickInteractionEvent(Item item, int slot) {
		super(InteractionEvent.InteractionType.SECOND_ITEM_CLICK, item, slot, 1);
	}
}
