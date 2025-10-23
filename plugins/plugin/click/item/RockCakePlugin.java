package plugin.click.item;

import com.runehive.game.event.impl.ItemClickEvent;
import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.combat.hit.Hit;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.position.Area;

import java.util.concurrent.TimeUnit;

public class RockCakePlugin extends PluginContext {

    @Override
    protected boolean firstClickItem(Player player, ItemClickEvent event) {
        if (event.getItem().getId() == 7510) {
            if (!player.itemDelay.elapsed(599, TimeUnit.MILLISECONDS))
                return true;

            if (player.getCombat().inCombat()) {
                player.message("You can not eat this while in combat!");
                return true;
            }

            if (Area.inWilderness(player)) {
                player.message("You better not eat this while in the wilderness!");
                return true;
            }

            int health = player.getCurrentHealth();
            int damage = health - 1;

            if (damage <= 0) {
                player.message("You better not eat that!");
                return true;
            }

            player.speak("Ouch!");
            player.damage(new Hit(player.getCurrentHealth() - 1));
            player.itemDelay.reset();
            return true;
        }
        return false;
    }
}
