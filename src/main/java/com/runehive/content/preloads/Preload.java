package com.runehive.content.preloads;


import com.runehive.content.skill.impl.magic.Spellbook;
import com.runehive.game.world.items.Item;

public interface Preload {

    String title();

    Spellbook spellbook();

    Item[] equipment();

    Item[] inventory();

    int[] skills();

}
