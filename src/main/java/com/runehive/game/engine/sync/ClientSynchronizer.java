package com.runehive.game.engine.sync;

import com.runehive.game.world.entity.MobList;
import com.runehive.game.world.entity.mob.npc.Npc;
import com.runehive.game.world.entity.mob.player.Player;

public interface ClientSynchronizer {

    void synchronize(MobList<Player> players, MobList<Npc> npcs);

}
