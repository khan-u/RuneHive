package com.runehive.net.packet.out;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.codec.ByteModification;
import com.runehive.net.codec.ByteOrder;
import com.runehive.net.packet.OutgoingPacket;
import com.runehive.net.packet.PacketType;

/**
 * Sends an enter text prompt to the player.
 * Allows typing text without space closing the interface.
 * 
 * @author AI Port from Elvarg
 */
public class SendEnterText extends OutgoingPacket {

    private final String prompt;

    public SendEnterText(String prompt) {
        super(187, PacketType.VAR_BYTE);
        this.prompt = prompt;
    }

    @Override
    public boolean encode(Player player) {
        builder.writeString(prompt);
        return true;
    }
}
