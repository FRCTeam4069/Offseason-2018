package frc.team4069.robot.auto.modes

import edu.wpi.first.wpilibj.command.CommandGroup
import frc.team4069.robot.auto.AutoMode
import frc.team4069.robot.commands.drive.FollowPathCommand
import frc.team4069.robot.commands.elevator.SetElevatorPositionCommand
import frc.team4069.robot.subsystems.ElevatorSubsystem

class FarRightScaleMode : AutoMode() {
    override fun build(): CommandGroup {
        return object : CommandGroup() {
            init {
                addParallel(SetElevatorPositionCommand(ElevatorSubsystem.Position.CARRY))
                addSequential(FollowPathCommand("scale-right-far.csv", zeroPose = true))
            }
        }
    }

}