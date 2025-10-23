package com.runehive.net.packet.out;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.OutgoingPacket;

/**
 * Handles sending the special attack amount (used for the orb).
 *
 * @author Daniel
 */
public final class SendSpecialAmount extends OutgoingPacket {

    public SendSpecialAmount() {
        super(137, 1);
    }

    @Override
    public boolean encode(Player player) {
        builder.writeByte(player.getSpecialPercentage().get());
        return true;
    }

}
