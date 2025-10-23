package plugin.click.item;

import com.runehive.content.activity.Activity;
import com.runehive.content.consume.FoodData;
import com.runehive.game.Animation;
import com.runehive.game.UpdatePriority;
import com.runehive.game.event.impl.ItemClickEvent;
import com.runehive.game.plugin.PluginContext;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.entity.skill.Skill;
import com.runehive.game.world.items.Item;
import com.runehive.net.packet.out.SendMessage;

import java.util.Optional;

public class EatFoodPlugin extends PluginContext {

    @Override
    protected boolean firstClickItem(Player player, ItemClickEvent event) {
        final Optional<FoodData> foodResult = FoodData.forId(event.getItem().getId());

        if (!foodResult.isPresent()) {
            return false;
        }

        final FoodData foodType = foodResult.get();

        if (Activity.evaluate(player, it -> !it.canEat(player, foodType))) {
            return true;
        }

        if (!player.interfaceManager.isClear()) {
            player.interfaceManager.close(false);
        }

        eat(player, event.getItem(), event.getSlot(), foodType);
        return true;
    }

    public static void eat(Player player, Item item, int slot, FoodData food) {
        if (food.isFast() && player.fastFoodDelay.elapsed(600) || player.foodDelay.elapsed(1600)) {
            if (food.isFast()) {
                player.fastFoodDelay.reset();
                player.potionDelay.reset();
            }

            int heal = food.getHeal();
            int maxHealth = player.getCurrentHealth() > player.getMaximumHealth() ? player.getCurrentHealth() : player.getMaximumHealth();

            if (food == FoodData.ANGLERFISH && maxHealth < player.getMaximumHealth() + FoodData.anglerfishHeal(player)) {
                maxHealth = player.getMaximumHealth() + FoodData.anglerfishHeal(player);
            }

            player.animate(new Animation(829, UpdatePriority.LOW));
            player.getCombat().reset();
            player.getCombat().cooldown(3);
            player.foodDelay.reset();

            if (food.getReplacement() != -1)
                player.inventory.replace(item.getId(), food.getReplacement(), slot, true);
            else
                player.inventory.remove(item, slot);

            player.skills.get(Skill.HITPOINTS).modifyLevel(level -> level + heal, 0, maxHealth);
            player.skills.refresh(Skill.HITPOINTS);
            player.send(new SendMessage("You eat the " + food.getName() + ".", true));
        }
    }

}
