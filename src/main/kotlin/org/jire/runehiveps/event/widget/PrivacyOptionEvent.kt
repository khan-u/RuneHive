package org.jire.runehiveps.event.widget

import com.runehive.game.world.entity.mob.player.Player

/**
 * @author Jire
 */
class PrivacyOptionEvent(
    val publicMode: Int,
    val privateMode: Int,
    val tradeMode: Int,
    val clanMode: Int
) : WidgetEvent {

    override fun handle(player: Player) {
        player.relations.setPrivacyChatModes(publicMode, privateMode, clanMode, tradeMode)
    }

}