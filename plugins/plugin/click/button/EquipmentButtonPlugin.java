package plugin.click.button;

import com.runehive.content.ItemsKeptOnDeath;
import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.mob.player.Player;

public class EquipmentButtonPlugin extends PluginContext {

    @Override
    protected boolean onClick(Player player, int button) {
        if (button == 27653) {
            player.equipment.openInterface();
            return true;
        }
        if (button == 27654) {
            ItemsKeptOnDeath.open(player);
            return true;
        }
        return false;
    }
}