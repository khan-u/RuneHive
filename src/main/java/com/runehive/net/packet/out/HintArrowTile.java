package com.runehive.net.packet.out;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.position.Position;
import com.runehive.net.packet.OutgoingPacket;

/** Shows the flashing yellow hint arrow pointing at a world tile (317 opcode 254, type=2). */
public final class HintArrowTile extends OutgoingPacket {

    private static final int OPCODE = 254; // set hint icon (all modes)
    private final Position pos;

    public HintArrowTile(Position pos) {
        // 1(type) + 2(x) + 2(y) + 1(z) = 6
        super(OPCODE, /*capacity*/ 6);
        this.pos = pos;
    }

    @Override
    protected boolean encode(Player player) {
        builder.writeByte(2);                 // type=2 (tile location - right on coordinates)
        builder.writeShort(pos.getX());       // absolute world X
        builder.writeShort(pos.getY());       // absolute world Y
        builder.writeByte(40);                // height offset in pixels (40 = lower height)
        return true;
    }
}