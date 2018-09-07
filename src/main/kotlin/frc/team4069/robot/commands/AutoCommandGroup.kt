package frc.team4069.robot.commands

import edu.wpi.first.wpilibj.command.CommandGroup
import edu.wpi.first.wpilibj.command.WaitCommand
import frc.team4069.robot.commands.drive.FollowPathCommand
import frc.team4069.robot.commands.intake.SetIntakeSpeedCommand
import frc.team4069.robot.subsystems.ElevatorSubsystem

class AutoCommandGroup : CommandGroup() {
    init {
        addParallel(DelayRunElevatorCommand(ElevatorSubsystem.Position.SWITCH, 2.5))
        addSequential(FollowPathCommand("switch-right-medium.csv", true))
        addSequential(SetIntakeSpeedCommand(-0.7))
        addSequential(WaitCommand(0.5))
        addSequential(SetIntakeSpeedCommand(0.0))
    }
}