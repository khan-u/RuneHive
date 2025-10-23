package plugin.click.object;

import com.runehive.game.event.impl.ObjectClickEvent;
import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.mob.player.Player;

public class ObjectThirdClickPlugin extends PluginContext {

    @Override
    protected boolean thirdClickObject(Player player, ObjectClickEvent event) {
        final int id = event.getObject().getId();

        switch (id) {

        }

        return false;
    }

}
