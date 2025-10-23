package org.jire.runehiveps.event.npc

import com.runehive.game.world.entity.mob.player.Player
import com.runehive.net.packet.out.SendMessage
import org.jire.runehiveps.defs.MonsterDefLoader

/**
 * @author Jire
 */
class NpcExamineEvent(val npcId: Int) : NpcEvent {

    override fun handle(player: Player) {
        val monsterDef = MonsterDefLoader.map[npcId] ?: return
        val examine = monsterDef.examine
        if ("null" != examine) {
            player.send(SendMessage(examine))
        }
    }

}