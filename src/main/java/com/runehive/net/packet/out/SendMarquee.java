package com.runehive.net.packet.out;

import com.runehive.game.world.World;
import com.runehive.game.world.entity.mob.player.Player;
import com.runehive.net.packet.OutgoingPacket;
import com.runehive.net.packet.PacketType;

public class SendMarquee extends OutgoingPacket {

    private final String[] strings;
    private final int id;

    public SendMarquee(int id, String... strings) {
        super(205, PacketType.VAR_SHORT);
        this.strings = strings;
        this.id = id;
    }

    @Override
    public boolean encode(Player player) {
        builder.writeInt(id);
        for (int index = 0; index < 5; index++) {
            builder.writeString(index >= strings.length
                    ? ""
                    : strings[index].replace("#players", World.getPlayerCount() + ""));
        }
        return true;
    }
}
