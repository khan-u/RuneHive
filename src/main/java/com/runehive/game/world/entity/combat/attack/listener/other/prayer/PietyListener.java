package com.runehive.game.world.entity.combat.attack.listener.other.prayer;

import com.runehive.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.runehive.game.world.entity.mob.Mob;

public class PietyListener extends SimplifiedListener<Mob> {

    @Override
    public int modifyAttackLevel(Mob attacker, Mob defender, int level) {
        return level * 6 / 5;
    }

    @Override
    public int modifyStrengthLevel(Mob attacker, Mob defender, int level) {
        return level * 123 / 100;
    }

    @Override
    public int modifyDefenceLevel(Mob attacker, Mob defender, int level) {
        return level * 5 / 4;
    }

}
