package plugin.click.object.wintertodt;

import com.runehive.game.event.impl.ObjectClickEvent;
import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.items.Item;
import com.runehive.game.world.object.GameObject;

public class TakeHammerPlugin extends PluginContext {

    @Override
    protected boolean firstClickObject(Player player, ObjectClickEvent event) {
        final GameObject gameObject = event.getObject();
        final int objectId = gameObject.getId();

        if(objectId != 29316) return false;

        if(player.inventory.contains(2347)) {
            player.message("You already have a hammer.");
            return true;
        }

        if(!player.inventory.hasCapacityFor(new Item(2347))) {
            player.message("You need space in your inventory to take a hammer.");
            return true;
        }

        player.message("You take a hammer from the crate.");
        player.inventory.add(2347, 1);
        player.inventory.refresh();

        return true;
    }

}