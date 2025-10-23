package com.runehive.net.packet.out;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.OutgoingPacket;

/** Clears any active hint arrow (317 opcode 254, type=0). */
public final class HintArrowClear extends OutgoingPacket {

    private static final int OPCODE = 254;

    public HintArrowClear() {
        super(OPCODE, /*capacity*/ 6);
    }

    @Override
    protected boolean encode(Player player) {
        builder.writeByte(0);      // type=0 (reset/clear)
        builder.writeShort(0);     // x (ignored by client for clear)
        builder.writeShort(0);     // y
        builder.writeByte(0);      // z
        return true;
    }
}