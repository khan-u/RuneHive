package com.runehive.content.mysterybox;

import com.runehive.game.world.items.Item;

import static com.runehive.content.mysterybox.MysteryRarity.COMMON;

public class MysteryItem extends Item {

    final MysteryRarity rarity;

    public MysteryItem(int id, int amount, MysteryRarity rarity) {
        super(id, amount);
        this.rarity = rarity;
    }

    public MysteryItem(int id, int amount) {
        this(id, amount, COMMON);
    }
}
