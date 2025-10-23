package com.runehive.net.packet.in;

import com.runehive.game.world.entity.mob.data.PacketType;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.GamePacket;
import com.runehive.net.packet.PacketListener;
import com.runehive.net.packet.PacketListenerMeta;
import org.jire.runehiveps.event.widget.KeyPacketEvent;

/**
 * The {@code GamePacket} responsible for clicking keyboard buttons.
 *
 * @author Daniel | Obey
 */
@PacketListenerMeta(186)
public class KeyPacketListener implements PacketListener {

    @Override
    public void handlePacket(Player player, GamePacket packet) {
        final int key = packet.readShort();

        if (key < 0)
            return;
        if (player.locking.locked(PacketType.KEY))
            return;

        player.getEvents().widget(player, new KeyPacketEvent(key));
    }
}
