package com.runehive.content.store.currency.impl;

import com.runehive.content.store.currency.Currency;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.out.SendMessage;

public final class LMSPointCurrency implements Currency {

    @Override
    public boolean tangible() {
        return false;
    }

    @Override
    public boolean takeCurrency(Player player, int amount) {
        if (player.lmsPoints >= amount) {
            player.lmsPoints -= amount;
            return true;
        } else {
            player.send(new SendMessage("You do not have enough LMS points."));
            return false;
        }
    }

    @Override
    public void recieveCurrency(Player player, int amount) {
        player.lmsPoints += amount;
    }

    @Override
    public int currencyAmount(Player player) {
        return player.lmsPoints;
    }

    @Override
    public boolean canRecieveCurrency(Player player) {
        return true;
    }
}
