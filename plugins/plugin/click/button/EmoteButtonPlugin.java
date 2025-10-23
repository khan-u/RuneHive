package plugin.click.button;

import com.runehive.content.emote.Emote;
import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.util.Utility;

public class EmoteButtonPlugin extends PluginContext {

    @Override
    protected boolean onClick(Player player, int button) {
        if (button == 18714) {
            player.locking.lock();
            Emote.skillcape(player);
            player.locking.unlock();
            return true;
        }

        if (!Emote.forId(button).isPresent()) {
            return false;
        }

        Emote emote = Emote.forId(button).get();

        if (!emote.activated(player)) {
            player.dialogueFactory.sendStatement("You have not unlocked the " + Utility.formatEnum(emote.name()) + " emote yet!").execute();
            return true;
        }

        Emote.execute(player, emote.getAnimation(), emote.getGraphic());
        return true;
    }
}
