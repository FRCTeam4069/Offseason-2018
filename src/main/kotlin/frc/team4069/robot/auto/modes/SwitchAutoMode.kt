package frc.team4069.robot.auto.modes

import edu.wpi.first.wpilibj.command.CommandGroup
import edu.wpi.first.wpilibj.command.WaitCommand
import frc.team4069.robot.auto.AutoMode
import frc.team4069.robot.auto.Trajectories
import frc.team4069.robot.commands.DelayRunElevatorCommand
import frc.team4069.robot.commands.drive.FollowPathCommand
import frc.team4069.robot.commands.intake.ToggleOpenIntakeCommand
import frc.team4069.robot.subsystems.ElevatorSubsystem
import openrio.powerup.MatchData

class SwitchAutoMode : AutoMode() {
    override fun build(): CommandGroup {
        val side = MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR)
        val path = Trajectories.switchRight
        return object : CommandGroup() {
            init {
                addParallel(DelayRunElevatorCommand(ElevatorSubsystem.Position.SWITCH, 0.3))
                addSequential(FollowPathCommand(path, zeroPose = true))
                addSequential(ToggleOpenIntakeCommand())
                addSequential(WaitCommand(0.1))
                addParallel(DelayRunElevatorCommand(ElevatorSubsystem.Position.MINIMUM, 0.7))
//                addSequential(FollowPathCommand(path, reversed = true))
//                addSequential(WaitCommand(0.1))
//                addSequential(SetIntakeSpeedCommand(1.0))
//                addSequential(DriveStraightCommand(6.5.ft, baseVelocity = 6.fps))
//                addSequential(WaitCommand(0.1))
//                addSequential(SetIntakeSpeedCommand(0.0))
//                addSequential(ToggleOpenIntakeCommand())
//                addParallel(DelayRunElevatorCommand(ElevatorSubsystem.Position.SWITCH, 0.4))
//                addSequential(DriveStraightCommand((-6.5).ft, baseVelocity = 6.fps))
//                addSequential(FollowPathCommand(path))
//                addSequential(ToggleOpenIntakeCommand())
//                addSequential(WaitCommand(0.5))
//                addSequential(ToggleOpenIntakeCommand())
            }
        }
    }

}