package com.runehive.game.world.entity.combat.strategy.player.special.melee;

import com.runehive.game.Animation;
import com.runehive.game.Graphic;
import com.runehive.game.UpdatePriority;
import com.runehive.game.world.entity.combat.hit.CombatHit;
import com.runehive.game.world.entity.combat.hit.Hit;
import com.runehive.game.world.entity.combat.strategy.player.PlayerMeleeStrategy;
import com.runehive.game.world.entity.mob.Direction;
import com.runehive.game.world.entity.mob.Mob;
import com.runehive.game.world.entity.mob.data.LockType;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.pathfinding.TraversalMap;
import com.runehive.game.world.position.Position;
import com.runehive.net.packet.out.SendMessage;

/**
 * Handles the dragon spear weapon special attack.
 *
 * @author Daniel
 */
public class DragonSpear extends PlayerMeleeStrategy {
    private static final DragonSpear INSTANCE = new DragonSpear();
    private static final Animation ANIMATION = new Animation(1064, UpdatePriority.HIGH);
    private static final Graphic GRAPHIC = new Graphic(253, true, UpdatePriority.HIGH);

    @Override
    public boolean canAttack(Player attacker, Mob defender) {
        if (defender.isPlayer() && defender.width() > 1 && defender.length() > 1) {
            attacker.send(new SendMessage("That creature is too large to knock back!"));
            return false;
        }

        Direction direction = Direction.getDirection(attacker.getPosition(), defender.getPosition());

        if (!TraversalMap.isTraversable(defender.getPosition(), direction, false)) {
            attacker.send(new SendMessage("That entity can not be knocked back as something is blocking it!"));
            return false;
        }

        return super.canAttack(attacker, defender);
    }

    @Override
    public void hit(Player attacker, Mob defender, Hit hit) {
        super.hit(attacker, defender, hit);

        Direction direction = Direction.getDirection(attacker.getPosition(), defender.getPosition());
        Position position = defender.getPosition().transform(direction.getFaceLocation());

        hit.setDamage(-1);
        attacker.graphic(GRAPHIC);
        defender.movement.reset();
        defender.getCombat().clearIncoming();

        defender.locking.lock(3, LockType.STUN);
        defender.movement.walkTo(position);
    }

    @Override
    public CombatHit[] getHits(Player attacker, Mob defender) {
        return new CombatHit[] { nextMeleeHit(attacker, defender) };
    }

    @Override
    public Animation getAttackAnimation(Player attacker, Mob defender) {
        return ANIMATION;
    }

    public static DragonSpear get() {
        return INSTANCE;
    }

}