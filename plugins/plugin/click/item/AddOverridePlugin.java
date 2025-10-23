package plugin.click.item;

import com.runehive.game.event.impl.ItemOnItemEvent;
import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.items.Item;
public class AddOverridePlugin extends PluginContext {

    @Override
    protected boolean itemOnItem(Player player, ItemOnItemEvent event) {
        final Item useWith = event.getWith();
        final Item itemUsed = event.getUsed();

        if (useWith.getId() == 2399) {
            player.overrides.addOverride(itemUsed.getId());
            return true;
        } else if (itemUsed.getId() == 2399) {
            player.overrides.addOverride(useWith.getId());
            return true;
        }

        return false;
    }

}