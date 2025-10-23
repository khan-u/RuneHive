package com.runehive.net.packet.out;

import com.runehive.net.codec.ByteOrder;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.entity.skill.Skill;
import com.runehive.net.packet.OutgoingPacket;

/**
 * Sends a skill to a client.
 * 
 * @author Michael | Chex
 */
public class SendSkill extends OutgoingPacket {

	private final int id;
	private final int level;
	private final int experience;

	public SendSkill(int id, int level, int experience) {
		super(134, 6);
		this.id = id;
		this.level = level;
		this.experience = experience;
	}

	public SendSkill(Skill skill) {
		this(skill.getSkill(), skill.getLevel(), skill.getRoundedExperience());
	}

	@Override
	public boolean encode(Player player) {
		final int color = player.settings.prestigeColors ? player.prestige.getPrestigeColor(id) : 0xFFFF00;
		player.send(new SendColor(Skill.INTERFACE_DATA[id][0], color));
		player.send(new SendColor(Skill.INTERFACE_DATA[id][1], color));
		builder.writeByte(id)
		.writeInt(experience, ByteOrder.ME)
		.writeByte(level);
		return true;
	}
}