package plugin.itemon.player;

import com.runehive.game.event.impl.ItemOnPlayerEvent;
import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.mob.player.Player;

public class ItemOnPlayerPlugin extends PluginContext {

    @Override
    protected boolean itemOnPlayer(Player player, ItemOnPlayerEvent event) {
        return false;
    }

}
