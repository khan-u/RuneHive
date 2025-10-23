package plugin.click.button;

import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.out.SendInputMessage;
import com.runehive.util.Utility;

public class PlayerRelationButtonPlugin extends PluginContext {

    @Override
    protected boolean onClick(Player player, int button) {
        if (button == 5068) {
            player.send(new SendInputMessage("Enter the name of the friend you want to add:", 12, input -> {
                long name = Utility.nameToLong(input);
                player.relations.addFriend(name);
            }));
            return true;
        }
        if (button == 5069) {
            player.send(new SendInputMessage("Enter the name of the friend you want to delete:", 12, input -> {
                long name = Utility.nameToLong(input);
                player.relations.deleteFriend(name);
            }));
            return true;
        }
        return false;
    }
}