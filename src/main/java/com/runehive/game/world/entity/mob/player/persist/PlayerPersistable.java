package com.runehive.game.world.entity.mob.player.persist;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.codec.login.LoginResponse;

public interface PlayerPersistable {

    void save(Player player);

    LoginResponse load(Player player, String expectedPassword);

}
