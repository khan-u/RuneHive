package com.runehive.content.skill.impl.hunter.net;

import com.runehive.Config;
import com.runehive.content.activity.randomevent.RandomEventHandler;
import com.runehive.content.skill.impl.hunter.net.impl.Impling;
import com.runehive.game.action.impl.HunterRespawnTask;
import com.runehive.game.task.Task;
import com.runehive.game.world.World;
import com.runehive.game.world.entity.mob.npc.Npc;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.entity.skill.Skill;
import com.runehive.game.world.items.ItemDefinition;
import com.runehive.game.world.position.Position;
import com.runehive.net.packet.out.SendMessage;
import com.runehive.util.Utility;

/**
 * Created by Daniel on 2018-02-07.
 */
public class Netting extends Task {
    private final Player player;
    private final Npc npc;
    private final Position position;
    private final int experience;
    private final int levelRequired;
    private final int jar;
    private final int reward;

    public Netting(Player player, Npc npc, int experience, int levelRequired, int jar, int reward) {
        super(false, 3);
        this.player = player;
        this.npc = npc;
        this.position = npc.getPosition();
        this.experience = experience;
        this.levelRequired = levelRequired;
        this.jar = jar;
        this.reward = reward;
    }

    @Override
    protected boolean canSchedule() {
        if (player.skills.getMaxLevel(Skill.HUNTER) < levelRequired) {
            player.message("You need a hunter level of " + levelRequired + " to do this!");
            return false;
        }

        var canBarehandImpling = Impling.forId(npc.id).isPresent() && player.skills.getMaxLevel(Skill.HUNTER) > levelRequired + 9;
        if (!player.equipment.contains(10010) && !canBarehandImpling) {
            player.send(new SendMessage("You need to be wielding a butterfly net to do this!"));
            return false;
        }

        if (!player.inventory.contains(jar)) {
            String name = ItemDefinition.get(jar).getName();
            player.message("You need " + Utility.getAOrAn(name) + " " + name + " to do this!");
            return false;
        }

        if (player.inventory.isFull()) {
            player.message("You do not have any free inventory spaces to do this!");
            return false;
        }

        return !player.locking.locked();
    }

    @Override
    protected void onSchedule() {
        player.locking.lock();
        player.face(position);
        var anim = !player.equipment.contains(10010) ? 7171 : 6606;
        player.animate(anim);
    }

    @Override
    protected void execute() {
        boolean success = true;

        if (!npc.getPosition().equals(position) || Utility.random(3) == 1) {
            success = false;
        }

        if (success) {
            player.inventory.remove(jar);
            player.inventory.add(reward, 1);
            player.skills.addExperience(Skill.HUNTER, experience * Config.HUNTER_MODIFICATION);
            player.message("You catch the " + npc.getName() + " and place it in the jar.");
            World.schedule(new HunterRespawnTask(npc));
            RandomEventHandler.trigger(player);
            player.playerAssistant.activateSkilling(1);
        } else {
            player.message("You fail to catch the " + npc.getName() + ".");
        }

        cancel();
    }

    @Override
    protected void onCancel(boolean logout) {
        player.locking.unlock();
    }
}
