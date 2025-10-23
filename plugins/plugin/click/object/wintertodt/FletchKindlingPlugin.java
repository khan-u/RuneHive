package plugin.click.object.wintertodt;

import com.runehive.content.wintertodt.Wintertodt;
import com.runehive.game.event.impl.ItemOnItemEvent;
import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.items.Item;

public class FletchKindlingPlugin extends PluginContext {

    @Override
    protected boolean itemOnItem(Player player, ItemOnItemEvent event) {
        final Item useWith = event.getWith();
        final Item itemUsed = event.getUsed();

        if (useWith.getId() == Wintertodt.BRUMA_ROOT && itemUsed.getId() == 946 || useWith.getId() == 946 && itemUsed.getId() == Wintertodt.BRUMA_ROOT) {
            Wintertodt.fletch(player);
            return true;
        }

        return false;
    }

}