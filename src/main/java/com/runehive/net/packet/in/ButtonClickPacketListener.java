package com.runehive.net.packet.in;

import com.runehive.content.event.EventDispatcher;
import com.runehive.content.event.impl.ClickButtonInteractionEvent;
import com.runehive.game.event.impl.ButtonClickEvent;
import com.runehive.game.plugin.PluginManager;
import com.runehive.game.world.entity.mob.data.PacketType;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.entity.mob.player.PlayerRight;
import com.runehive.net.packet.ClientPackets;
import com.runehive.net.packet.GamePacket;
import com.runehive.net.packet.PacketListener;
import com.runehive.net.packet.PacketListenerMeta;
import com.runehive.net.packet.out.SendMessage;

/**
 * The {@code GamePacket} responsible for clicking buttons on the client.
 *
 * @author Daniel | Obey
 */
@PacketListenerMeta(ClientPackets.BUTTON_CLICK)
public class ButtonClickPacketListener implements PacketListener {

    @Override
    public void handlePacket(final Player player, GamePacket packet) {
        final int button = packet.readShort();

        if (player.isDead()) {
            return;
        }

        if (player.locking.locked(PacketType.CLICK_BUTTON, button)) {
            return;
        }


        // player.message("Currently not available");

        if (PlayerRight.isDeveloper(player) || PlayerRight.isOwner(player)) {
            player.send(new SendMessage(String.format("[%s]: button=%d", ButtonClickPacketListener.class.getSimpleName(), button)));
            System.out.println(String.format("[%s]: button=%d", ButtonClickPacketListener.class.getSimpleName(), button));
        }//save it plz theres no save button with intellij


        if (EventDispatcher.execute(player, new ClickButtonInteractionEvent(button))) {
            return;
        }

        PluginManager.getDataBus().publish(player, new ButtonClickEvent(button));
    }
}
