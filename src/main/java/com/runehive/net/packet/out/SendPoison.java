package com.runehive.net.packet.out;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.codec.ByteModification;
import com.runehive.net.packet.OutgoingPacket;

public class SendPoison extends OutgoingPacket {

    public enum PoisonType {
        NO_POISON,
        REGULAR,
        VENOM
    }

    private final PoisonType type;

    public SendPoison(PoisonType type) {
        super(182, 1);
        this.type = type;
    }

    @Override
    public boolean encode(Player player) {
        builder.writeByte(type.ordinal(), ByteModification.NEG);
        return true;
    }

}
