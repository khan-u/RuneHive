package com.runehive.game.world.entity.combat.attack.listener.other.prayer.strength;

import com.runehive.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.runehive.game.world.entity.mob.Mob;

public class BurstOfStrengthListener extends SimplifiedListener<Mob> {

    @Override
    public int modifyStrengthLevel(Mob attacker, Mob defender, int damage) {
        return damage * 21 / 20;
    }

}
