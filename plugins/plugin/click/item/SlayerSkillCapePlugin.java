package plugin.click.item;

import com.runehive.content.dialogue.DialogueFactory;
import com.runehive.game.event.impl.ItemClickEvent;
import com.runehive.game.event.impl.ItemContainerContextMenuEvent;
import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.items.Item;
import com.runehive.net.packet.out.SendMessage;

public class SlayerSkillCapePlugin extends PluginContext {

    @Override
    protected boolean secondClickItem(Player player, ItemClickEvent event) {
        final Item item = event.getItem();
        if (item.getId() == 9786 || item.getId() == 9787) {
            performSlayerTaskSkip(player);
            return true;
        }
        return false;
    }

    @Override
    protected boolean thirdClickItem(Player player, ItemClickEvent event) {
        final Item item = event.getItem();
        if (item.getId() == 9786 || item.getId() == 9787) {
            player.send(new SendMessage("Task skips for today left: <col=ff0000>" + player.dailySlayerTaskSkip.remainingUses(player) + "."));
            return true;
        }
        return false;
    }

    @Override
    protected boolean secondClickItemContainer(Player player, ItemContainerContextMenuEvent event) {
        if (event.getInterfaceId() != 1688) {
            return false;
        }

        final Item item = player.equipment.getCape();
        if (item == null) {
            return false;
        }

        if (item.getId() == 9786 || item.getId() == 9787) {
            performSlayerTaskSkip(player);
            return true;
        }
        return false;
    }

    private void performSlayerTaskSkip(Player player) {
        if (!player.dailySlayerTaskSkip.canUse(player)) {
            return;
        }

        final DialogueFactory factory = player.dialogueFactory;

        factory.sendOption("Cancel task", () -> {
            if (player.slayer.cancel(false)) {
                player.dailySlayerTaskSkip.use();
            }},
                "Nevermind.", () -> player.dialogueFactory.clear());
        factory.execute();
    }
}
