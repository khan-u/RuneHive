package com.runehive.content.activity.impl.pestcontrol;

import com.runehive.content.activity.ActivityListener;
import com.runehive.game.world.entity.combat.hit.Hit;
import com.runehive.game.world.entity.mob.Mob;

public class PestControlListener extends ActivityListener<PestControlGame> {

    PestControlListener(PestControlGame activity) {
        super(activity);
    }

    @Override
    public boolean canAttack(Mob attacker, Mob defender) {
        return !attacker.isPlayer() || defender.id != 1756;
    }

    @Override
    public void hitsplat(Mob attacker, Mob defender, Hit hit) {
        if (!attacker.isPlayer())
            return;

        PestControlGame.PestControlNode node = (PestControlGame.PestControlNode) activity.getActivity(attacker);
        node.damage += hit.getDamage();
    }

}
