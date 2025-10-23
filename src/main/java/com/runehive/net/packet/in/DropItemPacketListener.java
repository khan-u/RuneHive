package com.runehive.net.packet.in;

import com.runehive.content.gambling.GambleStage;
import com.runehive.content.itemaction.ItemActionRepository;
import com.runehive.game.event.impl.DropItemEvent;
import com.runehive.game.plugin.PluginManager;
import com.runehive.game.world.entity.mob.data.PacketType;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.entity.mob.player.PlayerRight;
import com.runehive.game.world.items.Item;
import com.runehive.game.world.items.ItemDefinition;
import com.runehive.game.world.items.containers.pricechecker.PriceType;
import com.runehive.game.world.items.ground.GroundItem;
import com.runehive.game.world.position.Area;
import com.runehive.net.codec.ByteModification;
import com.runehive.net.packet.ClientPackets;
import com.runehive.net.packet.GamePacket;
import com.runehive.net.packet.PacketListener;
import com.runehive.net.packet.PacketListenerMeta;
import com.runehive.net.packet.out.SendMessage;

/**
 * The {@code GamePacket} responsible for dropping items.
 *
 * @author Daniel | Obey
 */
@PacketListenerMeta(ClientPackets.DROP_ITEM)
public class DropItemPacketListener implements PacketListener {

    @Override
    public void handlePacket(Player player, GamePacket packet) {
        if (player.isDead() || player.locking.locked(PacketType.DROP_ITEM) || player.getGambling().getStage().equals(GambleStage.PLACING_BET) || player.getGambling().getStage().equals(GambleStage.IN_PROGRESS)) {
            return;
        }

        final int itemId = packet.readShort(false, ByteModification.ADD);
        packet.readByte(false);
        packet.readByte(false);
        final int slot = packet.readShort(false, ByteModification.ADD);
        final Item item = player.inventory.get(slot);

        if (ItemDefinition.get(itemId) == null)
            return;

        player.getCombat().reset();

        if (!player.interfaceManager.isClear())
            player.interfaceManager.close(false);

        if (player.idle)
            player.idle = false;

        if (item == null)
            return;

        if (item.getId() != itemId)
            return;

        if (PluginManager.getDataBus().publish(player, new DropItemEvent(item, slot, player.getPosition().copy())))
            return;

        if (ItemActionRepository.drop(player, item)) {
            if (PlayerRight.isDeveloper(player)) {
                player.send(new SendMessage(String.format("[%s]: item=%d amount=%d slot=%d", ItemActionRepository.class.getSimpleName(), item.getId(), item.getAmount(), slot)));
            }
            return;
        }

        boolean inWilderness = Area.inWilderness(player);
        if (inWilderness && item.getValue(PriceType.VALUE) >= 500_000) {
            player.dialogueFactory.sendStatement("This is a valuable item, are you sure you want to", "drop it? In a PvP area, this item will be seen", "by everyone when dropped.");
            player.dialogueFactory.sendOption("Yes, drop it.", () -> {
                player.inventory.remove(item, slot, true);
                GroundItem.createGlobal(player, item);
            }, "Nevermind", () -> player.dialogueFactory.clear());
            player.dialogueFactory.execute();
            return;
        } else if (inWilderness) {
            player.inventory.remove(item, slot, true);
            GroundItem.createGlobal(player, item);
            return;
        }

        player.inventory.remove(item, slot, true);
        GroundItem.create(player, item);
    }
}
