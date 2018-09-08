package frc.team4069.robot.commands

import edu.wpi.first.wpilibj.command.CommandGroup
import edu.wpi.first.wpilibj.command.WaitCommand
import frc.team4069.robot.commands.drive.DriveStraightCommand
import frc.team4069.saturn.lib.math.uom.distance.ft

class AutoCommandGroup : CommandGroup() {
    init {
//        addParallel(DelayRunElevatorCommand(ElevatorSubsystem.Position.SWITCH, 2.5))
//        addSequential(FollowPathCommand("switch-right-new.csv", true))
//        addSequential(SetIntakeSpeedCommand(-0.7))
//        addSequential(WaitCommand(0.5))
//        addSequential(SetIntakeSpeedCommand(0.0))
//        addParallel(DelayRunElevatorCommand(ElevatorSubsystem.Position.MINIMUM, 0.7))
//        addSequential(DriveStraightCommand((-2).ft))
//        addSequential(TurnToAngleCommand(90.0))
//        addParallel(SetIntakeSpeedCommand(1.0))
//        addSequential(DriveStraightCommand(2.ft))

        addSequential(DriveStraightCommand(2.ft))
        addSequential(WaitCommand(1.5))
        addSequential(DriveStraightCommand((-2).ft))
    }
}