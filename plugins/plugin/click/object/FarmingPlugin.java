package plugin.click.object;

import com.runehive.content.skill.impl.farming.Farming;
import com.runehive.game.event.impl.ItemOnObjectEvent;
import com.runehive.game.event.impl.ObjectClickEvent;
import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.mob.player.Player;

public class FarmingPlugin extends PluginContext {

    @Override
    protected boolean itemOnObject(Player player, ItemOnObjectEvent event) {
        return Farming.itemOnObject(player, event.getObject(), event.getUsed(), event.getSlot());
    }

    @Override
    protected boolean firstClickObject(Player player, ObjectClickEvent event) {
        return Farming.firstClickObject(player, event.getObject());
    }

    @Override
    protected boolean secondClickObject(Player player, ObjectClickEvent event) {
        return Farming.secondClickObject(player, event.getObject());
    }

}
