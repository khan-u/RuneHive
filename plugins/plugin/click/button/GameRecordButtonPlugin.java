package plugin.click.button;

import com.runehive.content.activity.ActivityType;
import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.mob.player.Player;

public class GameRecordButtonPlugin extends PluginContext {

    @Override
    protected boolean onClick(Player player, int button) {
        if (button == 32307) {
            player.gameRecord.global = true;
            player.gameRecord.display(ActivityType.getFirst());
            return true;
        }
        if (button == 32309) {
            player.gameRecord.global = false;
            player.gameRecord.display(ActivityType.getFirst());
            return true;
        }
        int base_button = 32352;
        int index = (button - base_button) / 2;
        ActivityType activity = ActivityType.getOrdinal(index);
        if (activity == null)
            return false;
        player.gameRecord.display(activity);
        return true;
    }
}
