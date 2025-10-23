package com.runehive.net.packet.out;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.position.Position;
import com.runehive.net.packet.OutgoingPacket;

public class SendCameraTurnPosition extends OutgoingPacket {
    private final Position lookAt;
    private final int zTilt;
    private final int constantSpeed;
    private final int variableSpeed;

    public SendCameraTurnPosition(Position lookAt, int zTilt, int constantSpeed, int variableSpeed) {
        super(177, 7);
        this.lookAt = lookAt;
        this.zTilt = zTilt;
        this.constantSpeed = constantSpeed;
        this.variableSpeed = variableSpeed;
    }

    @Override
    protected boolean encode(Player player) {
        final Position base = player.getPosition();
        final int x = lookAt.getLocalX(base);
        final int y = lookAt.getLocalY(base);
        builder.writeByte(x);
        builder.writeByte(y);
        builder.writeShort(zTilt);
        builder.writeByte(constantSpeed);
        builder.writeByte(variableSpeed);
        return true;
    }
}
