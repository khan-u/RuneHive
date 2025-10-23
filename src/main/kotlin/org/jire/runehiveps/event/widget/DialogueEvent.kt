package org.jire.runehiveps.event.widget

import com.runehive.game.world.entity.mob.player.Player

/**
 * @author Jire
 */
object DialogueEvent : WidgetEvent {

    override fun handle(player: Player) {
        player.dialogueFactory.execute()
    }

}