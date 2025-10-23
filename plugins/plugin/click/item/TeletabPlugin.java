package plugin.click.item;

import com.runehive.content.skill.impl.magic.teleport.Teleportation;
import com.runehive.content.skill.impl.magic.teleport.TeleportationData;
import com.runehive.content.teleport.TeleportTablet;
import com.runehive.game.event.impl.ItemClickEvent;
import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.items.Item;
import com.runehive.net.packet.out.SendMessage;

public class TeletabPlugin extends PluginContext {

    @Override
    protected boolean firstClickItem(Player player, ItemClickEvent event) {
        if (!TeleportTablet.forId(event.getItem().getId()).isPresent()) {
            return false;
        }

        final TeleportTablet tablet = TeleportTablet.forId(event.getItem().getId()).get();

        if (player.house.isInside()) {
            player.send(new SendMessage("Please leave the house before teleporting."));
            return true;
        }

        player.inventory.remove(new Item(event.getItem().getId(), 1));
        Teleportation.teleport(player, tablet.getPosition(), 20, TeleportationData.TABLET);
        return true;
    }
}
