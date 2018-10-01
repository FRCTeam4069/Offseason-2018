package frc.team4069.robot.commands

import edu.wpi.first.wpilibj.command.CommandGroup
import edu.wpi.first.wpilibj.command.WaitCommand
import frc.team4069.robot.commands.drive.DriveStraightCommand
import frc.team4069.robot.commands.drive.FollowPathCommand
import frc.team4069.robot.commands.drive.TurnToAngleCommand
import frc.team4069.robot.commands.intake.SetIntakeSpeedCommand
import frc.team4069.robot.commands.intake.ToggleOpenIntakeCommand
import frc.team4069.robot.subsystems.ElevatorSubsystem
import frc.team4069.saturn.lib.math.uom.distance.ft
import frc.team4069.saturn.lib.math.uom.velocity.fps

class AutoCommandGroup : CommandGroup() {
    init {
        addParallel(DelayRunElevatorCommand(ElevatorSubsystem.Position.SWITCH, 1.5))
        addSequential(FollowPathCommand("switch-right.csv", true))
        addSequential(ToggleOpenIntakeCommand())
        addSequential(WaitCommand(0.1))
        addParallel(DelayRunElevatorCommand(ElevatorSubsystem.Position.MINIMUM, 0.7))
        addSequential(DriveStraightCommand({ (-2.7).ft }, baseVelocity = 3.5.fps))
        addSequential(WaitCommand(0.2))
        addSequential(TurnToAngleCommand(90.0))
        addSequential(WaitCommand(0.3))
        addSequential(SetIntakeSpeedCommand(1.0))
//        val command = ForwardIntakeCommand()
//        addSequential(command)
        addSequential(DriveStraightCommand({ 4.5.ft }, baseVelocity = 3.fps))
        addSequential(WaitCommand(0.3))
        addSequential(SetIntakeSpeedCommand(0.0))
        addSequential(ToggleOpenIntakeCommand())
        addParallel(DelayRunElevatorCommand(ElevatorSubsystem.Position.SWITCH, 0.5))
        addSequential(DriveStraightCommand({ (-4.5).ft }, baseVelocity = 4.fps))
        addSequential(TurnToAngleCommand(-90.0))
        addSequential(DriveStraightCommand({ 2.7.ft}))
        addSequential(ToggleOpenIntakeCommand())
        addSequential(WaitCommand(0.6))
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