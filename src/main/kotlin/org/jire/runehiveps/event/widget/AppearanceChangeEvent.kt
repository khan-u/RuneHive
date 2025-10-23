package org.jire.runehiveps.event.widget

import com.runehive.content.achievement.AchievementHandler
import com.runehive.content.achievement.AchievementKey
import com.runehive.game.world.entity.mob.UpdateFlag
import com.runehive.game.world.entity.mob.player.Player
import com.runehive.game.world.entity.mob.player.appearance.Appearance
import com.runehive.game.world.entity.mob.player.appearance.Gender
import com.runehive.net.packet.`in`.AppearanceChangePacketListener

/**
 * @author Jire
 */
class AppearanceChangeEvent(
    val gender: Int,
    val head: Int,
    val jaw: Int,
    val torso: Int,
    val arms: Int,
    val hands: Int,
    val legs: Int,
    val feet: Int,
    val hairColor: Int,
    val torsoColor: Int,
    val legsColor: Int,
    val feetColor: Int,
    val skinColor: Int
) : WidgetEvent {

    override fun handle(player: Player) {
        val appearance = Appearance(
            if (gender == 0) Gender.MALE else Gender.FEMALE,
            head,
            if (gender == 0) jaw else -1,
            torso,
            arms,
            hands,
            legs,
            feet,
            hairColor,
            torsoColor,
            legsColor,
            feetColor,
            skinColor
        )
        if (AppearanceChangePacketListener.isValid(player, appearance)) {
            player.appearance = appearance
            player.updateFlags.add(UpdateFlag.APPEARANCE)
            player.interfaceManager.close()

            AchievementHandler.activate(player, AchievementKey.CHANGE_APPEARANCE, 1)
        }
    }

}