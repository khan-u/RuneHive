package plugin;

import com.runehive.game.event.impl.DropItemEvent;
import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.mob.player.Player;

public class DestroyItemPlugin extends PluginContext {

    @Override
    protected boolean onDropItem(Player player, DropItemEvent event) {
        if (event.getItem().matchesId(12_931) || event.getItem().matchesId(13_197) || event.getItem().matchesId(13_199)) {
            return false;
        }

        if (event.getItem().isDestroyable()) {
            player.playerAssistant.destroyItem(event.getItem(), event.getSlot());
            return true;
        }

        return false;
    }

}
