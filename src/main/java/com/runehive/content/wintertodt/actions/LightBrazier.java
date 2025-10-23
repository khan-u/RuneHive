package com.runehive.content.wintertodt.actions;

import com.runehive.Config;
import com.runehive.content.wintertodt.Brazier;
import com.runehive.content.wintertodt.Wintertodt;
import com.runehive.game.action.Action;
import com.runehive.game.action.policy.WalkablePolicy;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.entity.skill.Skill;

public class LightBrazier extends Action<Player> {

    private Brazier brazier;

    public LightBrazier(Player player, Brazier brazier) {
        super(player, 3);
        this.brazier = brazier;
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.NON_WALKABLE;
    }

    @Override
    public String getName() {
        return "Light brazier";
    }

    @Override
    protected void execute() {
        if(brazier.getBrazierState() != 2) {
            //brazier.getObject().transform(Wintertodt.BURNING_BRAZIER_ID);
            brazier.getObject().unregister();
            brazier.setObject(Wintertodt.BURNING_BRAZIER_ID);
            brazier.getObject().register();
        }

        getMob().skills.addExperience(Skill.FIREMAKING, (Skill.getLevelForExperience(getMob().skills.get(Skill.FIREMAKING).getExperience()) * 6) * Config.FIREMAKING_MODIFICATION);
        Wintertodt.addPoints(getMob(), 25);
        getMob().animate(65535);
        getMob().action.getCurrentAction().cancel();
    }
}