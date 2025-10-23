package com.runehive.content.event.impl;

import com.runehive.content.event.InteractionEvent;
import com.runehive.game.world.object.GameObject;

public class ObjectInteractionEvent extends InteractionEvent {

	private final GameObject object;
	private final int opcode;

	public ObjectInteractionEvent(InteractionType type, GameObject object, int opcode) {
		super(type);
		this.object = object;
		this.opcode = opcode;
	}

	public GameObject getObject() {
		return object;
	}

	public int getOpcode() {
		return opcode;
	}

}