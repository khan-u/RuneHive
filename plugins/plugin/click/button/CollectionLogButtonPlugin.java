package plugin.click.button;

import com.runehive.content.collectionlog.CollectionLog;
import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.mob.player.Player;

public class CollectionLogButtonPlugin extends PluginContext {
    @Override
    protected boolean onClick(Player player, int button) {
        if(CollectionLog.clickButton(player, button))
            return true;

        switch (button) {

        }
        return false;
    }
}
