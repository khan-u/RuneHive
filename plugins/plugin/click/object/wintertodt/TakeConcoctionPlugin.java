package plugin.click.object.wintertodt;

import com.runehive.content.wintertodt.Wintertodt;
import com.runehive.game.event.impl.ObjectClickEvent;
import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.items.Item;
import com.runehive.game.world.object.GameObject;

public class TakeConcoctionPlugin extends PluginContext {

    @Override
    protected boolean firstClickObject(Player player, ObjectClickEvent event) {
        final GameObject gameObject = event.getObject();
        final int objectId = gameObject.getId();

        if(objectId != 29320) return false;

        return take(player, 1);
    }

    @Override
    protected boolean secondClickObject(Player player, ObjectClickEvent event) {
        final GameObject gameObject = event.getObject();
        final int objectId = gameObject.getId();

        if(objectId != 29320) return false;

        return take(player, 5);
    }

    @Override
    protected boolean thirdClickObject(Player player, ObjectClickEvent event) {
        final GameObject gameObject = event.getObject();
        final int objectId = gameObject.getId();

        if(objectId != 29320) return false;

        return take(player, 10);
    }

    private boolean take(Player player, int amount) {
        if(amount > player.inventory.getFreeSlots())
            amount = player.inventory.getFreeSlots();

        if(!player.inventory.hasCapacityFor(new Item(Wintertodt.REJUV_POT_UNF, amount))) {
            player.message("You need space in your inventory to take an unfinished potion.");
            return true;
        }

        player.message("You take an unfinished potion from the crate.");
        player.inventory.add(Wintertodt.REJUV_POT_UNF, amount);
        player.inventory.refresh();

        return true;
    }

}