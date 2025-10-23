package com.runehive.content.store.currency.impl;

import com.runehive.content.store.currency.Currency;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.out.SendMessage;

public final class PestPointCurrency implements Currency {

    @Override
    public boolean tangible() {
        return false;
    }

    @Override
    public boolean takeCurrency(Player player, int amount) {
        if (player.pestPoints >= amount) {
            player.pestPoints -= amount;
            return true;
        } else {
            player.send(new SendMessage("You do not have enough pest control points."));
            return false;
        }
    }

    @Override
    public void recieveCurrency(Player player, int amount) {
        player.pestPoints += amount;
    }

    @Override
    public int currencyAmount(Player player) {
        return player.pestPoints;
    }

    @Override
    public boolean canRecieveCurrency(Player player) {
        return true;
    }
}
