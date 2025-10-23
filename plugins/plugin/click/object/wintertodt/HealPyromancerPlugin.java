package plugin.click.object.wintertodt;

import com.runehive.content.wintertodt.Wintertodt;
import com.runehive.game.event.impl.ItemOnNpcEvent;
import com.runehive.game.event.impl.NpcClickEvent;
import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.mob.npc.Npc;
import com.runehive.game.world.entity.mob.player.Player;

public class HealPyromancerPlugin extends PluginContext {

    @Override
    protected boolean firstClickNpc(Player player, NpcClickEvent event) {
        final Npc clickedNpc = event.getNpc();
        final int npcId = clickedNpc.id;

        if(npcId != Wintertodt.INCAPACITATED_PYROMANCER) return false;

        int slot = -1;

        if(player.inventory.contains(Wintertodt.REJUV_POT_4))
            slot = player.inventory.getSlotById(Wintertodt.REJUV_POT_4);
        else if(player.inventory.contains(Wintertodt.REJUV_POT_3))
            slot = player.inventory.getSlotById(Wintertodt.REJUV_POT_3);
        else if(player.inventory.contains(Wintertodt.REJUV_POT_2))
            slot = player.inventory.getSlotById(Wintertodt.REJUV_POT_2);
        else if(player.inventory.contains(Wintertodt.REJUV_POT_1))
            slot = player.inventory.getSlotById(Wintertodt.REJUV_POT_1);

        if(slot != -1)
            Wintertodt.healPyromancer(player, clickedNpc, slot);

        return true;
    }

    protected boolean itemOnNpc(Player player, ItemOnNpcEvent event) {
        final Npc npc = event.getNpc();
        final int npcId = npc.id;

        if(npcId != Wintertodt.INCAPACITATED_PYROMANCER) return false;

        Wintertodt.healPyromancer(player, npc, event.getSlot());

        return true;
    }

}