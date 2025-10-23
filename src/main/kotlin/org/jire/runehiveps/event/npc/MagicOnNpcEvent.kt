package org.jire.runehiveps.event.npc

import com.runehive.content.skill.impl.hunter.net.impl.Butterfly
import com.runehive.content.skill.impl.hunter.net.impl.Impling
import com.runehive.game.world.entity.combat.magic.CombatSpell
import com.runehive.game.world.entity.mob.npc.Npc
import com.runehive.game.world.entity.mob.player.Player
import com.runehive.net.packet.out.SendMessage

/**
 * @author Jire
 */
class MagicOnNpcEvent(
    override val slot: Int,
    val spell: Int
) : NpcClickEvent {

    override fun handleNpc(player: Player, npc: Npc) {
        val definition = CombatSpell.get(spell) ?: return
        if (player.spellbook != definition.spellbook) return

        if (!npc.definition.isAttackable

            && !Impling.forId(npc.id).isPresent
            && !Butterfly.forId(npc.id).isPresent
        ) {
            player.send(SendMessage("This npc can not be attacked!"))
            return
        }

        player.setSingleCast(definition)

        if (!player.combat.attack(npc)) {
            player.setSingleCast(null)
            player.resetFace()
        }
    }

}