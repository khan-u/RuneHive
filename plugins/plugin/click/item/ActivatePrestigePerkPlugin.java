package plugin.click.item;

import com.runehive.content.prestige.PrestigePerk;
import com.runehive.game.event.impl.ItemClickEvent;
import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.out.SendMessage;
import com.runehive.util.MessageColor;

import java.util.HashSet;

public class ActivatePrestigePerkPlugin extends PluginContext {

    @Override
    protected boolean firstClickItem(Player player, ItemClickEvent event) {
        final PrestigePerk perk = PrestigePerk.forItem(event.getItem().getId());
        if (perk == null) {
            return false;
        }

        if (player.prestige.activePerks == null) {
            player.prestige.activePerks = new HashSet<>();
        }

        if (player.prestige.activePerks.contains(perk)) {
            player.send(new SendMessage("The Perk: " + perk.name + " perk is already active on your account!", MessageColor.DARK_BLUE));
            return true;
        }

        player.inventory.remove(event.getItem());
        player.prestige.activePerks.add(perk);
        player.send(new SendMessage("You have successfully activated the " + perk.name + " perk.", MessageColor.DARK_BLUE));
        return true;
    }
}
