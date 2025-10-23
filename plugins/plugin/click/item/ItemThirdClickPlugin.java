package plugin.click.item;

import com.runehive.game.event.impl.ItemClickEvent;
import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.out.SendMessage;

public class ItemThirdClickPlugin extends PluginContext {

    @Override
    protected boolean thirdClickItem(Player player, ItemClickEvent event) {
        switch (event.getItem().getId()) {

		/* Ring of recoil. */
            case 2550:
                int charges = player.ringOfRecoil;

                if (charges >= 40) {
                    player.dialogueFactory.sendStatement("<col=A11A1A>Ring of recoil", "Your ring already has the maximum charges.").execute();
                    break;
                }

                player.dialogueFactory.sendStatement("<col=A11A1A>Ring of recoil", "You currently have <col=255>" + charges + "</col> charges until the ring breaks.", "Are you sure you would like to break the ring?").sendOption("Yes", () -> {
                    player.ringOfRecoil = 40;
                    player.inventory.remove(event.getItem());
                    player.interfaceManager.close();
                    player.send(new SendMessage("<col=9A289E>Your ring has been drained of all charges; causing it to break."));
                }, "No", player.interfaceManager::close).execute();
                break;
            case 80:
                int whipCharges = player.whipCharges;
                System.out.println(whipCharges);
                break;

            default:
                return false;

        }
        return true;
    }

}
