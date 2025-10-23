package com.runehive.content.wintertodt.actions;

import com.runehive.content.wintertodt.Brazier;
import com.runehive.content.wintertodt.Wintertodt;
import com.runehive.game.action.Action;
import com.runehive.game.action.policy.WalkablePolicy;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.entity.skill.Skill;

public class FixBrazier extends Action<Player> {

    private Brazier brazier;

    public FixBrazier(Player player, Brazier brazier) {
        super(player, 3);
        this.brazier = brazier;
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.NON_WALKABLE;
    }

    @Override
    public String getName() {
        return "Fix brazier";
    }

    @Override
    protected void execute() {
        if(brazier.getBrazierState() != 2) {
            System.out.println("???????????");
            //brazier.getObject().transform(Wintertodt.EMPTY_BRAZIER_ID);
            brazier.getObject().unregister();
            brazier.setObject(Wintertodt.EMPTY_BRAZIER_ID);
            brazier.getObject().register();
        }

        getMob().skills.addExperience(Skill.CONSTRUCTION, Skill.getLevelForExperience(getMob().skills.get(Skill.CONSTRUCTION).getExperience()) * 4);
        Wintertodt.addPoints(getMob(), 25);
        getMob().animate(65535);
        getMob().action.getCurrentAction().cancel();
    }
}
