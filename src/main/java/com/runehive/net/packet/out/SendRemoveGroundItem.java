package com.runehive.net.packet.out;

import com.runehive.net.codec.ByteModification;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.game.world.items.ground.GroundItem;
import com.runehive.net.packet.OutgoingPacket;

public class SendRemoveGroundItem extends OutgoingPacket {

    private final GroundItem groundItem;

    public SendRemoveGroundItem(GroundItem groundItem) {
        super(156, 3);
        this.groundItem = groundItem;
    }

    @Override
    public boolean encode(Player player) {
        player.send(new SendCoordinate(groundItem.getPosition()));
        builder.writeByte(0, ByteModification.ADD)
        .writeShort(groundItem.item.getId());
        return true;
    }

}
