package com.runehive.net.packet.out;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.OutgoingPacket;

public class SendLMSFog extends OutgoingPacket {

    private final boolean enable;
    private final int fogStrength;

    public SendLMSFog(int fogStrength) {
        super(116, 5);
        this.enable = true;
        this.fogStrength = fogStrength;
    }

    public SendLMSFog(boolean enable) {
        super(116, 5);
        this.enable = enable;
        this.fogStrength = 0;
    }

    @Override
    protected boolean encode(Player player) {
        builder.writeByte(enable ? 1 : 0).writeInt(fogStrength);
        return true;
    }
}
