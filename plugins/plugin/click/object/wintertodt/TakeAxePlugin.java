package plugin.click.object.wintertodt;

import com.runehive.content.skill.impl.woodcutting.AxeData;
import com.runehive.game.event.impl.ObjectClickEvent;
import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.items.Item;
import com.runehive.game.world.object.GameObject;
public class TakeAxePlugin extends PluginContext {

    @Override
    protected boolean firstClickObject(Player player, ObjectClickEvent event) {
        final GameObject gameObject = event.getObject();
        final int objectId = gameObject.getId();

        if(objectId != 29318) return false;

        if(AxeData.getDefinition(player).orElse(null) != null) {
            player.message("You already have a axe.");
            return true;
        }

        if(!player.inventory.hasCapacityFor(new Item(1351))) {
            player.message("You need space in your inventory to take a axe.");
            return true;
        }

        player.message("You take a axe from the crate.");
        player.inventory.add(1351, 1);
        player.inventory.refresh();

        return true;
    }

}