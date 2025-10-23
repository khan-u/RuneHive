package com.runehive.game.world.entity.combat.attack.listener.item;

import com.runehive.net.packet.out.SendMessage;
import com.runehive.game.Graphic;
import com.runehive.game.UpdatePriority;
import com.runehive.game.world.entity.combat.attack.listener.ItemCombatListenerSignature;
import com.runehive.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import com.runehive.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.runehive.game.world.entity.combat.hit.Hit;
import com.runehive.game.world.entity.mob.Mob;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.entity.skill.Skill;
import com.runehive.util.Utility;

/**
 * Handles the Ahrim's armor effects to the assigned npc and item ids.
 *
 * @author Daniel
 */
@NpcCombatListenerSignature(npcs = {1672})
@ItemCombatListenerSignature(requireAll = true, items = {4745, 4747, 4749, 4751})
public class AhrimListener extends SimplifiedListener<Mob> {

    @Override
    public void hit(Mob attacker, Mob defender, Hit hit) {
        if (defender.isPlayer() && hit.getDamage() > 0) {
            boolean success = Utility.random(100) <= /*20*/95;

            if (!success)
                return;

            Player player = defender.getPlayer();
            int strength = player.skills.getLevel(Skill.STRENGTH);
            int drain = 5;

            strength -= drain;

            if (strength < 0)
                strength = 0;

            player.skills.setLevel(Skill.STRENGTH, strength);
            player.send(new SendMessage(drain + "% strength has been drained by " + attacker.getName() + "."));
            player.graphic(new Graphic(400, UpdatePriority.VERY_HIGH));

            if (attacker.isPlayer()) {
                attacker.getPlayer().send(new SendMessage("You have drained " + drain +"% of " + defender.getName() + "'s strength level."));
            }
        }
    }
}
