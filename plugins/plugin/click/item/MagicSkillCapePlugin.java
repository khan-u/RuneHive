package plugin.click.item;

import com.runehive.Config;
import com.runehive.content.skill.impl.magic.Spellbook;
import com.runehive.game.event.impl.ItemClickEvent;
import com.runehive.game.event.impl.ItemContainerContextMenuEvent;
import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.combat.magic.Autocast;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.items.Item;
import com.runehive.net.packet.out.SendMessage;

public class MagicSkillCapePlugin extends PluginContext {

    @Override
    protected boolean secondClickItem(Player player, ItemClickEvent event) {
        final Item item = event.getItem();
        if (item.getId() == 9762 || item.getId() == 9763) {
            performSpellBookSwap(player);
            return true;
        }
        return false;
    }

    @Override
    protected boolean thirdClickItem(Player player, ItemClickEvent event) {
        final Item item = event.getItem();
        if (item.getId() == 9762 || item.getId() == 9763) {
            player.send(new SendMessage("Spellbook swaps for today left: <col=ff0000>" + player.dailySpellBookSwap.remainingUses(player) + "."));
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

        if (item.getId() == 9762 || item.getId() == 9763) {
            performSpellBookSwap(player);
            return true;
        }
        return false;
    }

    private void performSpellBookSwap(Player player) {
        if (!player.dailySpellBookSwap.canUse(player)) {
            return;
        }
        player.dialogueFactory.sendOption("Modern", () -> {
            Autocast.reset(player);
            player.dailySpellBookSwap.use();
            player.spellbook = Spellbook.MODERN;
            player.interfaceManager.setSidebar(Config.MAGIC_TAB, player.spellbook.getInterfaceId());
        }, "Ancient", () -> {
            Autocast.reset(player);
            player.dailySpellBookSwap.use();
            player.spellbook = Spellbook.ANCIENT;
            player.interfaceManager.setSidebar(Config.MAGIC_TAB, player.spellbook.getInterfaceId());
        }, "Lunar", () -> {
            Autocast.reset(player);
            player.dailySpellBookSwap.use();
            player.spellbook = Spellbook.LUNAR;
            player.interfaceManager.setSidebar(Config.MAGIC_TAB, player.spellbook.getInterfaceId());
        }).execute();
    }
}
