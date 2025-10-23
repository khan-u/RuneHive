package com.runehive.net.packet.out;

import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.position.Position;
import com.runehive.net.packet.OutgoingPacket;

public class SendCameraMovePosition extends OutgoingPacket {
    private final Position position;
    private final int zPos;
    private final int constantSpeed;
    private final int variableSpeed;

    public SendCameraMovePosition(Position position, int zPos, int constantSpeed, int variableSpeed) {
        super(166, 7);
        this.position = position;
        this.zPos = zPos;
        this.constantSpeed = constantSpeed;
        this.variableSpeed = variableSpeed;
    }

    @Override
    protected boolean encode(Player player) {
        final Position base = player.getPosition();
        final int x = position.getLocalX(base);
        final int y = position.getLocalY(base);
        builder.writeByte(x);
        builder.writeByte(y);
        builder.writeShort(zPos);
        builder.writeByte(constantSpeed);
        builder.writeByte(variableSpeed);
        return true;
    }
}
