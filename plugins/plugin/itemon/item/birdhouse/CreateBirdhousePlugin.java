package plugin.itemon.item.birdhouse;

import com.runehive.content.skill.impl.hunter.birdhouse.BirdhouseData;
import com.runehive.content.skill.impl.hunter.birdhouse.action.CreateBirdHouse;
import com.runehive.game.event.impl.ItemOnItemEvent;
import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.entity.skill.SkillData;
import com.runehive.game.world.items.Item;

public class CreateBirdhousePlugin extends PluginContext {

    @Override
    protected boolean itemOnItem(Player player, ItemOnItemEvent event) {
        final Item useWith = event.getWith();
        final Item itemUsed = event.getUsed();

        if(itemUsed.getId() != 8792 && useWith.getId() != 8792) return false;

        int logUsed = itemUsed.getId() == 8792 ? useWith.getId() : itemUsed.getId();

        BirdhouseData birdHouseData = BirdhouseData.forLog(logUsed);
        if(birdHouseData == null) return false;

        if(!player.inventory.containsAll(2347, 1755)) {
            player.dialogueFactory.sendStatement("You need a hammer and chisel to make a birdhouse", "trap.");
            return true;
        }

        if(player.skills.getLevel(SkillData.CRAFTING.ordinal()) < birdHouseData.craftingData[0]) {
            player.dialogueFactory.sendStatement("You need level "+birdHouseData.craftingData[0]+" Crafting to combine those.");
            return true;
        }

        player.animate(7057);
        player.action.execute(new CreateBirdHouse(player, birdHouseData));

        return true;
    }

}
