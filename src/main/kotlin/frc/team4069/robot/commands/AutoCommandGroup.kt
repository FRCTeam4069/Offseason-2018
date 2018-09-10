package frc.team4069.robot.commands

import edu.wpi.first.wpilibj.command.CommandGroup
import edu.wpi.first.wpilibj.command.WaitCommand
import frc.team4069.robot.commands.drive.DriveStraightCommand
import frc.team4069.robot.commands.drive.FollowPathCommand
import frc.team4069.robot.commands.drive.TurnToAngleCommand
import frc.team4069.robot.commands.intake.ForwardIntakeCommand
import frc.team4069.robot.commands.intake.SetIntakeSpeedCommand
import frc.team4069.robot.subsystems.ElevatorSubsystem
import frc.team4069.saturn.lib.math.uom.distance.ft
import frc.team4069.saturn.lib.math.uom.velocity.fps

class AutoCommandGroup : CommandGroup() {
    init {
        addParallel(DelayRunElevatorCommand(ElevatorSubsystem.Position.SWITCH, 2.5))
        addSequential(FollowPathCommand("switch-right-new.csv", true))
        addSequential(SetIntakeSpeedCommand(-0.7))
        addSequential(WaitCommand(0.5))
        addSequential(SetIntakeSpeedCommand(0.0))
        addParallel(DelayRunElevatorCommand(ElevatorSubsystem.Position.MINIMUM, 0.7))
        addSequential(DriveStraightCommand((-1.3).ft, baseVelocity = 5.fps))
        addSequential(WaitCommand(0.5))
        addSequential(TurnToAngleCommand(88.5))
        addSequential(WaitCommand(0.3))
        addParallel(SetIntakeSpeedCommand(1.0))
        addSequential(ForwardIntakeCommand())
        addSequential(WaitCommand(0.3))
        addSequential(SetIntakeSpeedCommand(0.0))
        addParallel(DelayRunElevatorCommand(ElevatorSubsystem.Position.SWITCH, 1.0))
        addSequential(DriveStraightCommand((-5).ft))
        addSequential(TurnToAngleCommand(-90.0))
        addSequential(DriveStraightCommand(1.5.ft))
        addSequential(SetIntakeSpeedCommand(-1.0))
    }
}