package com.runehive.content.event.impl;

import com.runehive.content.event.InteractionEvent;
import com.runehive.game.world.items.Item;
import com.runehive.game.world.object.GameObject;

public class ItemOnObjectInteractionEvent extends InteractionEvent {

	private final Item item;
	private final GameObject object;

	public ItemOnObjectInteractionEvent(Item item, GameObject object) {
		super(InteractionType.ITEM_ON_OBJECT);
		this.item = item;
		this.object = object;
	}

	public Item getItem() {
		return item;
	}

	public GameObject getObject() {
		return object;
	}
}
