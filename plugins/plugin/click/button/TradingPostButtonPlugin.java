package plugin.click.button;

import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.mob.player.Player;

public class TradingPostButtonPlugin extends PluginContext {

    @Override
    protected boolean onClick(Player player, int button) {
        return player.tradingPost.handleButtonClick(button);
    }
}
