package com.runehive.content.skill.impl.magic.spell.impl;

import com.runehive.Config;
import com.runehive.content.achievement.AchievementHandler;
import com.runehive.content.achievement.AchievementKey;
import com.runehive.content.activity.randomevent.RandomEventHandler;
import com.runehive.content.skill.impl.magic.Magic;
import com.runehive.content.skill.impl.magic.Spellbook;
import com.runehive.content.skill.impl.magic.spell.Spell;
import com.runehive.game.Animation;
import com.runehive.game.Graphic;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.entity.skill.Skill;
import com.runehive.game.world.items.Item;
import com.runehive.net.packet.out.SendForceTab;
import com.runehive.net.packet.out.SendMessage;

import java.util.Arrays;

/**
 * The high alchemy spell.
 *
 * @author Daniel
 */
public class HighAlchemy implements Spell {

	@Override
	public String getName() {
		return "High alchemy";
	}

	@Override
	public int getLevel() {
		return 55;
	}

	@Override
	public Item[] getRunes() {
		return new Item[] { new Item(554, 5), new Item(561, 1) };
	}

	@Override
	public void execute(Player player, Item item) {
		if (player.spellbook != Spellbook.MODERN)
			return;

        if (!player.spellCasting.castingDelay.elapsed(500)) {
            return;
		}

		if (Arrays.stream(Magic.UNALCHEABLES).anyMatch($it -> item.getId() == $it.getId())) {
			player.send(new SendMessage("You can not alch this item!"));
			return;
		}

		int value = item.getHighAlch();

		player.animate(new Animation(713));
		player.graphic(new Graphic(113, true));
		player.inventory.remove(item.getId(), 1);
		player.inventory.removeAll(getRunes());
		player.inventory.add(Config.CURRENCY, value == 0 ? 1 : value);
		player.inventory.refresh();
		player.send(new SendForceTab(6));
		player.skills.addExperience(Skill.MAGIC, 65 * (Config.MAGIC_MODIFICATION + 5));
		AchievementHandler.activate(player, AchievementKey.HIGH_ALCHEMY, 1);
		player.spellCasting.castingDelay.reset();
		player.action.clearNonWalkableActions();
		RandomEventHandler.trigger(player);
	}
}
