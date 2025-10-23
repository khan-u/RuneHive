package com.runehive.game.service;

import com.runehive.game.world.entity.mob.player.Player;

public class Highscores {

    private int[] experience = new int[26];

    private Player player;

    public Highscores(Player player) {
        this.player = player;
    }

    public void execute() {

    }

    private int[] getExperience() {
        for (int i = 0; i < player.skills.getSkills().length; i++) {
            // System.out.println("Experience: " + Skill.getExperienceForLevel(99));
            experience[i] = (int) player.skills.getSkills()[i].getExperience();
        }
        return experience;
    }
}



