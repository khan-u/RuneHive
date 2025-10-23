package com.runehive.game.world.entity.combat.attack.listener.other.prayer.attack;

import com.runehive.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.runehive.game.world.entity.mob.Mob;

public class ImprovedReflexesListener extends SimplifiedListener<Mob> {

    @Override
    public int modifyAttackLevel(Mob attacker, Mob defender, int damage) {
        return damage * 11 / 10;
    }

}
