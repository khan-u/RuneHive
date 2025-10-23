package plugin.click.object.wintertodt;

import com.runehive.content.wintertodt.Wintertodt;
import com.runehive.game.event.impl.ObjectClickEvent;
import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.object.GameObject;

public class FeedBrazierPlugin extends PluginContext {

    @Override
    protected boolean firstClickObject(Player player, ObjectClickEvent event) {
        final GameObject gameObject = event.getObject();
        final int objectId = gameObject.getId();

        if(objectId != Wintertodt.BURNING_BRAZIER_ID) return false;

        Wintertodt.feedBrazier(player, gameObject);

        return true;
    }

}
