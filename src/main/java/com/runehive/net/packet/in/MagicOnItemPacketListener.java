package com.runehive.net.packet.in;

import com.runehive.content.skill.impl.magic.spell.impl.HighAlchemy;
import com.runehive.content.skill.impl.magic.spell.impl.LowAlchemy;
import com.runehive.content.skill.impl.magic.spell.impl.SuperHeat;
import com.runehive.game.world.entity.mob.data.PacketType;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.entity.mob.player.PlayerRight;
import com.runehive.game.world.items.Item;
import com.runehive.net.codec.ByteModification;
import com.runehive.net.packet.ClientPackets;
import com.runehive.net.packet.GamePacket;
import com.runehive.net.packet.PacketListener;
import com.runehive.net.packet.PacketListenerMeta;

/**
 * The {@link GamePacket} responsible for using magic on inventory items.
 *
 * @author Daniel
 */
@PacketListenerMeta(ClientPackets.MAGIC_ON_ITEMS)
public class MagicOnItemPacketListener implements PacketListener {

    @Override
    @SuppressWarnings("unused")
    public void handlePacket(Player player, GamePacket packet) {
        if (player.locking.locked(PacketType.USE_MAGIC))
            return;

        final int slot = packet.readShort();
        final int itemId = packet.readShort(ByteModification.ADD);
        final int childId = packet.readShort();
        final int spell = packet.readShort(ByteModification.ADD);

        if (player.positionChange) {
            return;
        }

        final Item item = player.inventory.get(slot);

        if (item == null || item.getId() != itemId) {
            return;
        }

        if (PlayerRight.isDeveloper(player) ) {
            player.message("[MagicOnItemPacket] spell=" + spell + " itemId=" + itemId + " slot=" + slot + " childId=" + childId);
        }

        switch (spell) {
            case 1155:  //Lvl-1 enchant sapphire
            case 1165:  //Lvl-2 enchant emerald
            case 1176:  //Lvl-3 enchant ruby
            case 1180:  //Lvl-4 enchant diamond
            case 1187:  //Lvl-5 enchant dragonstone
            case 6003:  //Lvl-6 enchant onyx
            case 40180: //Lvl-7 enchant zenyte
                player.spellCasting.enchantItem(itemId, spell);
                break;

            case 1162:
                player.spellCasting.cast(new LowAlchemy(), item);
                break;

            case 1178:
                player.spellCasting.cast(new HighAlchemy(), item);
                break;

            case 1173:
                player.spellCasting.cast(new SuperHeat(), item);
                break;
        }
    }
}
