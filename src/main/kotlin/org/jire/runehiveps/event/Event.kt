package org.jire.runehiveps.event

import com.runehive.content.gambling.GambleStage
import com.runehive.game.world.entity.mob.player.Player

/**
 * @author Jire
 */
interface Event {

    fun canHandle(player: Player) =
        !player.isDead
                && player.gambling.stage != GambleStage.PLACING_BET
                && player.gambling.stage != GambleStage.IN_PROGRESS

    fun handle(player: Player) {}

}