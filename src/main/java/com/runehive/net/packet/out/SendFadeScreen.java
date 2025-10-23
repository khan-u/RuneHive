package com.runehive.net.packet.out;

import com.runehive.net.packet.OutgoingPacket;
import com.runehive.net.packet.PacketType;
import com.runehive.game.world.entity.mob.player.Player;

/**
 * Handles sending the fade screen packet.
 *
 * @author Daniel
 */
public class SendFadeScreen extends OutgoingPacket {

    private final String message;
    private final int state;
    private final int seconds;

    public SendFadeScreen(String message, int state, int seconds) {
        super(189, PacketType.VAR_SHORT);
        this.message = message;
        this.state = state;
        this.seconds = seconds;
    }

    @Override
    public boolean encode(Player player) {
        player.interfaceManager.close();
        builder.writeString(message)
        .writeByte(state)
        .writeByte(seconds);
        return true;
    }

}
