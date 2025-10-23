package plugin.itemon.object;


import com.runehive.content.skill.impl.crafting.impl.Jewellery;
import com.runehive.game.event.impl.ItemOnObjectEvent;
import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.items.Item;
import com.runehive.game.world.object.GameObjectDefinition;

public class JewelryPlugin extends PluginContext {


    @Override
    protected boolean itemOnObject(Player player, ItemOnObjectEvent event) {
        final GameObjectDefinition def = event.getObject().getDefinition();
        if (def.getName() != null && !def.getName().toLowerCase().contains("furnace")) {
            return false;
        }
        final Item usedItem = event.getUsed();
            if (usedItem.getId() == 2357) {
                Jewellery.open(player);
                return true;
            }
        return false;
    }
}

