package plugin.click.object;

import com.runehive.content.CrystalChest;
import com.runehive.game.action.impl.ChestAction;
import com.runehive.game.event.impl.ItemOnItemEvent;
import com.runehive.game.event.impl.ItemOnObjectEvent;
import com.runehive.game.event.impl.ObjectClickEvent;
import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.items.Item;
import com.runehive.net.packet.out.SendMessage;

public class CrystalChestPlugin extends PluginContext {

    @Override
    protected boolean itemOnItem(Player player, ItemOnItemEvent event) {
        boolean valid = false;
        if (event.getUsed().getId() == 985 && event.getWith().getId() == 987) {
            valid = true;
        } else if (event.getUsed().getId() == 987 && event.getWith().getId() == 985) {
            valid = true;
        }

        if (valid) {
            CrystalChest.createKey(player);
            return true;
        }

        return false;
    }

    @Override
    protected boolean firstClickObject(Player player, ObjectClickEvent event) {
        if (event.getObject().getId() != 2191) {
            return false;
        }

        if (!player.inventory.contains(CrystalChest.KEY)) {
            player.dialogueFactory.sendItem("Crystal Key", "You must have a crystal key to open this chest!", CrystalChest.KEY.getId()).execute();
            return true;
        }

        if (player.inventory.remaining() < 3) {
            player.send(new SendMessage("You need at least 3 free inventory spaces to enter the chest."));
            return true;
        }

        player.action.execute(new ChestAction(player, 989, new Item(1631), CrystalChest.getReward()), true);
        return true;
    }

    @Override
    protected boolean itemOnObject(Player player, ItemOnObjectEvent event) {
        if (event.getUsed().getId() == 989 && event.getObject().getId() == 2191) {
            if (!player.inventory.contains(CrystalChest.KEY)) {
                player.dialogueFactory.sendItem("Crystal Key", "You must have a crystal key to open this chest!", CrystalChest.KEY.getId()).execute();
                player.send(new SendMessage("You need a crystal key to open this chest!"));
                return true;
            }

            if (player.inventory.remaining() < 3) {
                player.send(new SendMessage("You need at least 3 free inventory spaces to enter the chest."));
                return true;
            }

            player.action.execute(new ChestAction(player,989, new Item(1631), CrystalChest.getReward()), true);
            return true;
        }

        return false;
    }

}
