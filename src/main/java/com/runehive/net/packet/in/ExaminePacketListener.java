package com.runehive.net.packet.in;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.entity.mob.player.PlayerRight;
import com.runehive.game.world.items.Item;
import com.runehive.game.world.items.containers.inventory.Inventory;
import com.runehive.game.world.items.containers.pricechecker.PriceType;
import com.runehive.net.packet.GamePacket;
import com.runehive.net.packet.PacketListener;
import com.runehive.net.packet.PacketListenerMeta;
import com.runehive.net.packet.out.SendMessage;
import com.runehive.util.MessageColor;
import com.runehive.util.Utility;
import org.jire.runehiveps.defs.ItemDef;
import org.jire.runehiveps.defs.ItemDefLoader;
import org.jire.runehiveps.event.item.ItemExamineEvent;
import org.jire.runehiveps.event.npc.NpcExamineEvent;
import org.jire.runehiveps.event.object.ObjectExamineEvent;

/**
 * Handles the in examine packet.
 *
 * @author Daniel
 */
@PacketListenerMeta({150})
public class ExaminePacketListener implements PacketListener {

    private static final int INTERFACE = 0;
    private static final int NPC = 1;
    private static final int ITEM = 2;
    private static final int OBJECT = 3;

    @Override
    public void handlePacket(Player player, GamePacket packet) {
        final int type = packet.readByte(false);
        switch (type) {
            case INTERFACE: {
                handleInterfaceExamine(player, packet);
                break;
            }
            case NPC: {
                int npcId = packet.readShort(false);
                player.getEvents().interact(player, new NpcExamineEvent(npcId));
                break;
            }
            case ITEM: {
                int itemId = packet.readShort(false);
                player.getEvents().interact(player, new ItemExamineEvent(itemId));
                break;
            }
            case OBJECT: {
                int objectId = packet.readShort(false);
                player.getEvents().interact(player, new ObjectExamineEvent(objectId));
                break;
            }
        }
    }

    private static void handleInterfaceExamine(Player player, GamePacket packet) {
        int slot = packet.readShort();
        int interfaceId = packet.readShort();
        int itemId = packet.readShort();

        if (PlayerRight.isDeveloper(player)) {
            player.send(new SendMessage("[Examine] - slot: " + slot + " -- interfaceId: " + interfaceId + " -- itemId: " + itemId, MessageColor.DEVELOPER));
        }

        if (slot == -1) {
            player.settings.clientWidth = interfaceId;
            player.settings.clientHeight = itemId;
            return;
        }

        switch (interfaceId) {
            case Inventory.INVENTORY_DISPLAY_ID: {
                Item item = player.inventory.get(slot);
                if (item == null || item.getId() != itemId) return;
                final ItemDef itemDef = ItemDefLoader.map.get(itemId);
                if (itemDef != null) {
                    String examine = itemDef.getExamine();
                    if (!"null".equals(examine))
                        player.send(new SendMessage(examine));
                }
                if (item.isTradeable()) {
                    String message = "";
                    message += "Item: <col=A52929>" + item.getName() + "</col> ";
                    message += "Value: <col=A52929>" + Utility.formatDigits(item.getValue(PriceType.VALUE)) + " (" + Utility.formatDigits(item.getValue(PriceType.VALUE) * item.getAmount()) + ")</col> ";
                    message += "High alch: <col=A52929>" + Utility.formatDigits(item.getValue(PriceType.HIGH_ALCH_VALUE)) + "</col> ";
                    player.send(new SendMessage(message));
                }
                break;
            }
            case 26816: {
                if (!player.attributes.has("DROP_SIMULATOR_SORTED_LIST")) return;
                Item[] items = player.attributes.get("DROP_SIMULATOR_SORTED_LIST");
                if (slot < 0 || slot >= items.length) return;
                Item item = items[slot];
                if (item.isTradeable()) {
                    String message = "";
                    message += "Item: <col=A52929>" + item.getName() + "</col> ";
                    message += "Value: <col=A52929>" + Utility.formatDigits(item.getValue(PriceType.VALUE)) + " (" + Utility.formatDigits(item.getValue(PriceType.VALUE) * item.getAmount()) + ")</col> ";
                    message += "High alch: <col=A52929>" + Utility.formatDigits(item.getValue(PriceType.HIGH_ALCH_VALUE));
                    player.send(new SendMessage(message));
                }
                break;
            }

        }
    }

}
