package frc.team4069.robot.commands

import edu.wpi.first.wpilibj.command.CommandGroup
import edu.wpi.first.wpilibj.command.WaitCommand
import frc.team4069.robot.commands.drive.DriveStraightCommand
import frc.team4069.robot.commands.drive.FollowPathCommand
import frc.team4069.robot.commands.intake.SetIntakeSpeedCommand
import frc.team4069.robot.commands.intake.ToggleOpenIntakeCommand
import frc.team4069.robot.subsystems.ElevatorSubsystem
import frc.team4069.saturn.lib.math.uom.distance.ft
import frc.team4069.saturn.lib.math.uom.velocity.fps

class AutoCommandGroup : CommandGroup() {
    init {
        addParallel(DelayRunElevatorCommand(ElevatorSubsystem.Position.SWITCH, 0.6))
        addSequential(FollowPathCommand("switch-right-new.csv", true))
        addSequential(ToggleOpenIntakeCommand())
        addSequential(WaitCommand(0.1))
        addParallel(DelayRunElevatorCommand(ElevatorSubsystem.Position.MINIMUM, 0.7))
        addSequential(FollowPathCommand("switch-right-new.csv", reversed = true))
        addSequential(WaitCommand(0.1))
        addSequential(SetIntakeSpeedCommand(1.0))
        addSequential(DriveStraightCommand(10.ft, baseVelocity = 4.fps))
        addSequential(WaitCommand(0.15))
        addSequential(SetIntakeSpeedCommand(0.0))
        addSequential(ToggleOpenIntakeCommand())
        addParallel(DelayRunElevatorCommand(ElevatorSubsystem.Position.SWITCH, 0.4))
        addSequential(DriveStraightCommand((-10).ft, baseVelocity = 4.fps))
        addSequential(FollowPathCommand("switch-right-new.csv"))
        addSequential(ToggleOpenIntakeCommand())
    }
}

//val switchRight = commandGroup {
//    -DelayRunElevatorCommand(ElevatorSubsystem.Position.SWITCH, 2.5)
//    +FollowPathCommand("switch-right-new.csv", true)
//    +SetIntakeSpeedCommand(-0.7)
//    +WaitCommand(0.5)
//    +SetIntakeSpeedCommand(0.0)
//    -DelayRunElevatorCommand(ElevatorSubsystem.Position.MINIMUM, 0.7)
//    +DriveStraightCommand((-2.7).ft)
//    +WaitCommand(0.2)
//    +TurnToAngleCommand(90.0)
//    +WaitCommand(0.3)
//    -SetIntakeSpeedCommand(1.0)
//    val cmd = ForwardIntakeCommand()
//    +cmd
//    +WaitCommand(0.3)
//    +SetIntakeSpeedCommand(0.0)
//    -SetElevatorPositionCommand(ElevatorSubsystem.Position.SWITCH)
//    +DriveStraightCommand({ (-cmd.finalDist).ft })
//    +TurnToAngleCommand(-90.0)
//    +DriveStraightCommand(1.5.ft)
//    +SetIntakeSpeedCommand(-1.0)
//    +WaitCommand(0.3)
//    +SetIntakeSpeedCommand(0.0)
//}
//