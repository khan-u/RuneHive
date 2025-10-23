package plugin.click.item;


import com.runehive.game.Animation;
import com.runehive.game.Graphic;
import com.runehive.game.event.impl.ItemClickEvent;
import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.World;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.position.Area;
import com.runehive.game.world.position.Position;
import com.runehive.net.packet.out.SendMessage;
import com.runehive.util.Utility;

import java.util.concurrent.TimeUnit;

/**
 * Handles the operating the dicing bag.
 *
 * @author Daniel.
 */
public class DiceBag extends PluginContext{

    /** The dice animation. */
    private static final Animation ANIMATION = new Animation(7219);

    @Override
    protected boolean firstClickItem(Player player, ItemClickEvent event) {
        if (event.getItem().getId() != 15098) {
            return false;
        }

        roll(player, false);
        return true;
    }

    @Override
    protected boolean secondClickItem(Player player, ItemClickEvent event) {
        if (event.getItem().getId() != 15098) {
            return false;
        }

        roll(player, true);
        return true;
    }

    /** Handles rolling the dice bag. */
    public void roll(Player player, boolean clan) {
        if (player.getCombat().inCombat()) {
            player.send(new SendMessage("You can't be in combat to do this!"));
            return;
        }
        if (Area.inWilderness(player)) {
            player.send(new SendMessage("You can't be in the wilderness to do this!"));
            return;
        }
        if (clan && player.clan == null) {
            player.send(new SendMessage("You need to be in a clan chat channel to do this!"));
            return;
        }
        if (!player.diceDelay.elapsed(3, TimeUnit.SECONDS)) {
            player.send(new SendMessage("You can't do this so quickly!"));
            return;
        }

        int random = Utility.random(100);

        if (clan) {
            if (player.clanChannel == null) {
                player.message("You must be in a clan to do this!");
                return;
            }

            player.animate(ANIMATION);
            player.clanChannel.message(player.getName() + " has rolled <col=ff0000>" + random + "</col> on the percentile dice!");
            return;
        }

        player.animate(ANIMATION);
        player.diceDelay.reset();
        player.send(new SendMessage("You have rolled <col=ff0000>" + random + "</col> on the percentile dice!"));
    }
}

