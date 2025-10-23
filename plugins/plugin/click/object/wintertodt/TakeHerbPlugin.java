package plugin.click.object.wintertodt;

import com.runehive.game.event.impl.ObjectClickEvent;
import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.object.GameObject;
import com.runehive.content.wintertodt.Wintertodt;
public class TakeHerbPlugin extends PluginContext {

    @Override
    protected boolean firstClickObject(Player player, ObjectClickEvent event) {
        final GameObject gameObject = event.getObject();
        final int objectId = gameObject.getId();

        if(objectId != 29315) return false;

        Wintertodt.takeHerb(player);

        return true;
    }

}