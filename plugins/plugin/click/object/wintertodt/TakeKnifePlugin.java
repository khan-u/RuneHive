package plugin.click.object.wintertodt;

import com.runehive.game.event.impl.ObjectClickEvent;
import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.items.Item;
import com.runehive.game.world.object.GameObject;
public class TakeKnifePlugin extends PluginContext {

    @Override
    protected boolean firstClickObject(Player player, ObjectClickEvent event) {
        final GameObject gameObject = event.getObject();
        final int objectId = gameObject.getId();

        if(objectId != 29317) return false;

        if(player.inventory.contains(946)) {
            player.message("You already have a knife.");
            return true;
        }

        if(!player.inventory.hasCapacityFor(new Item(946))) {
            player.message("You need space in your inventory to take a knife.");
            return true;
        }

        player.message("You take a knife from the crate.");
        player.inventory.add(946, 1);
        player.inventory.refresh();

        return true;
    }

}