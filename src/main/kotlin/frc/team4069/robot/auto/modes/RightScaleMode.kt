package frc.team4069.robot.auto.modes

import edu.wpi.first.wpilibj.command.CommandGroup
import frc.team4069.robot.auto.AutoMode
import openrio.powerup.MatchData

class RightScaleMode : AutoMode() {
    override fun build(): CommandGroup {
        val side = MatchData.getOwnedSide(MatchData.GameFeature.SCALE)

        return if(side == MatchData.OwnedSide.RIGHT) {
            CloseRightScaleMode().build()
        }else {
            FarRightScaleMode().build()
        }
    }

}