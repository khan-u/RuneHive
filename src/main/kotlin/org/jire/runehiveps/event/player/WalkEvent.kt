package org.jire.runehiveps.event.player

import com.runehive.content.activity.Activity
import com.runehive.content.activity.impl.duelarena.DuelArenaActivity
import com.runehive.content.activity.impl.duelarena.DuelRule
import com.runehive.game.Animation
import com.runehive.game.world.entity.mob.data.LockType
import com.runehive.game.world.entity.mob.data.PacketType
import com.runehive.game.world.entity.mob.player.Player
import com.runehive.game.world.entity.mob.player.PlayerRight
import com.runehive.game.world.position.Position
import com.runehive.net.packet.out.SendMessage
import org.jire.runehiveps.event.Event
import java.util.*

/**
 * @author Jire
 */
class WalkEvent(
    val targetX: Int,
    val targetY: Int,
    val runQueue: Boolean
) : Event {

    override fun handle(player: Player) {
        if (player.isGambleLocked) return

        if (player.locking.locked(PacketType.WALKING)) {
            if (player.locking.locked(LockType.STUN)) {
                player.send(SendMessage("You are currently stunned."))
                player.combat.reset()
            }
            if (player.locking.locked(LockType.FREEZE)) {
                player.send(SendMessage("A magical force stops you from moving!", true))
                player.combat.reset()
            }
            return
        }

        if (Activity.search(player, DuelArenaActivity::class.java).isPresent) {
            val activity = Activity.search(
                player,
                DuelArenaActivity::class.java
            ).get()
            if (activity.rules.contains(DuelRule.NO_MOVEMENT)) {
                player.send(SendMessage("You cannot move in the duel arena."))
                player.combat.reset()
                return
            }
        }

        player.skills.resetSkilling()

        if (player.resting) {
            player.animate(Animation.RESET, true)
            player.resting = false
        }

        /* Dialogues */

        /* Dialogues */if (player.dialogue.isPresent) {
            player.dialogue = Optional.empty()
        }

        /* Idle */

        /* Idle */if (player.idle) {
            player.idle = false
        }

        /* Dialogue factory */

        /* Dialogue factory */if (!player.dialogueFactory.chain.isEmpty()) {
            player.dialogueFactory.clear()
        }

        /* Dialogue options */

        /* Dialogue options */if (player.optionDialogue.isPresent) {
            player.optionDialogue = Optional.empty()
        }

        if (!player.interfaceManager.isMainClear) {
            player.interfaceManager.close()
        }

        if (!player.interfaceManager.isDialogueClear) {
            player.dialogueFactory.clear()
        }

        /* Reset the face. */

        /* Reset the face. */player.resetFace()

        /* Clear non walkable actions */

        /* Clear non walkable actions */player.action.clearNonWalkableActions()
        player.resetWaypoint()
        player.combat.reset()

        // the tile the player is trying to get to

        // the tile the player is trying to get to
        val destination = Position.create(targetX, targetY, player.height)

        // prevents the player from hacking the client to make the player walk really far distances.

        // prevents the player from hacking the client to make the player walk really far distances.
        if (player.position.getDistance(destination) > 32) {
            return
        }

        if (runQueue && PlayerRight.isDeveloper(player)) {
            player.move(destination)
            return
        }
        player.movement.isRunningQueue = runQueue
        player.movement.dijkstraPath(destination)
    }

}