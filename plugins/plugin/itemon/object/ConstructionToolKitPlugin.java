package plugin.itemon.object;

import com.runehive.game.event.impl.ItemOnObjectEvent;
import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.mob.player.Player;

public class ConstructionToolKitPlugin extends PluginContext {

    @Override
    protected boolean itemOnObject(Player player, ItemOnObjectEvent event) {
        return event.getUsed().getId() == 1 && player.house.toolkit(event.getObject());
    }

}
