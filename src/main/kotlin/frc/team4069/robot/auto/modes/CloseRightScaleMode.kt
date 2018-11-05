package frc.team4069.robot.auto.modes

import edu.wpi.first.wpilibj.command.CommandGroup
import edu.wpi.first.wpilibj.command.WaitCommand
import frc.team4069.robot.auto.AutoMode
import frc.team4069.robot.commands.DelayRunElevatorCommand
import frc.team4069.robot.commands.drive.DriveStraightCommand
import frc.team4069.robot.commands.drive.FollowPathCommand
import frc.team4069.robot.commands.drive.TurnToAngleCommand
import frc.team4069.robot.commands.elevator.SetElevatorPositionCommand
import frc.team4069.robot.commands.intake.SetIntakeSpeedCommand
import frc.team4069.robot.commands.intake.ToggleOpenIntakeCommand
import frc.team4069.robot.subsystems.ElevatorSubsystem
import frc.team4069.saturn.lib.math.uom.distance.ft

class CloseRightScaleMode : AutoMode() {
    override fun build(): CommandGroup {
        return object : CommandGroup() {
            init {
                addParallel(DelayRunElevatorCommand(ElevatorSubsystem.Position.SCALE, 1.75))
                addSequential(
                    FollowPathCommand(
                        "scale-right.csv",
                        zeroPose = true
                    )
                )
                addSequential(SetIntakeSpeedCommand(-0.6))
                addSequential(WaitCommand(0.2))
                addSequential(DriveStraightCommand((-1).ft))
                addSequential(WaitCommand(0.1))
                addParallel(object : CommandGroup() {
                    init {
                        addSequential(WaitCommand(0.4))
                        addSequential(TurnToAngleCommand(170.0))
                    }
                })
                addSequential(SetElevatorPositionCommand(ElevatorSubsystem.Position.MINIMUM))
                addSequential(WaitCommand(0.2))
                addSequential(SetIntakeSpeedCommand(1.0))
                addSequential(FollowPathCommand("scale-right/second-cube.csv"))
                addSequential(SetIntakeSpeedCommand(0.0))
                addSequential(ToggleOpenIntakeCommand())
                addSequential(DriveStraightCommand((-1).ft))
                addSequential(WaitCommand(0.1))
                addSequential(TurnToAngleCommand(-140.0))
                addParallel(SetElevatorPositionCommand(ElevatorSubsystem.Position.SCALE))
                addParallel(object : CommandGroup() {
                    init {
                        addSequential(WaitCommand(1.8))
                        addSequential(SetIntakeSpeedCommand(-1.0))
                        addSequential(WaitCommand(0.3))
                        addSequential(SetIntakeSpeedCommand(0.0))
                    }
                })
                addSequential(FollowPathCommand("scale-right/second-cube-deposit.csv"))
            }
        }
    }

}