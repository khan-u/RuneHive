package plugin.click.object;

import com.runehive.content.Obelisks;
import com.runehive.game.event.impl.ObjectClickEvent;
import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.mob.player.Player;

public class ObeliskObjectClickPlugin extends PluginContext {

    @Override
    protected boolean firstClickObject(Player player, ObjectClickEvent event) {
        return Obelisks.get().activate(player, event.getObject().getId());
    }

}
