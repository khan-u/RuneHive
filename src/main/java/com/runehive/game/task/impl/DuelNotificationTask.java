package com.runehive.game.task.impl;

import com.runehive.game.task.Task;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.entity.mob.player.exchange.ExchangeSession;
import com.runehive.game.world.entity.mob.player.exchange.ExchangeSessionType;
import com.runehive.net.packet.out.SendConfig;

public class DuelNotificationTask extends Task {

    private final Player player;
    private int time = 20;

    public DuelNotificationTask(Player player) {
        super(true, 0);
        this.player = player;
    }

    @Override
    public void execute() {
        if (!ExchangeSession.inSession(player, ExchangeSessionType.DUEL)) {
            cancel();
            return;
        }

        if (time <= 0) {
            cancel();
            return;
        }

        time--;

        if (time % 2 == 0) {
            player.send(new SendConfig(655, 1));
        } else {
            player.send(new SendConfig(655, 0));
        }

    }

    @Override
    protected void onCancel(boolean logout) {
        player.send(new SendConfig(655, 0));
    }

}
