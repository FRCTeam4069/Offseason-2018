package frc.team4069.robot.auto.modes

import edu.wpi.first.wpilibj.command.CommandGroup
import edu.wpi.first.wpilibj.command.WaitCommand
import frc.team4069.robot.auto.AutoMode
import frc.team4069.robot.commands.drive.DriveStraightCommand
import frc.team4069.saturn.lib.math.uom.distance.ft

class LeftScaleMode : AutoMode() {
    override fun build(): CommandGroup {
        return object : CommandGroup() {
            init {
                addSequential(WaitCommand(7.0))
                addSequential(DriveStraightCommand(5.ft))
            }
        }
    }
}