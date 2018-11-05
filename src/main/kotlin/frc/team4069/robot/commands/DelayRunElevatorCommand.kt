package frc.team4069.robot.commands

import edu.wpi.first.wpilibj.command.CommandGroup
import edu.wpi.first.wpilibj.command.WaitCommand
import frc.team4069.robot.commands.elevator.SetElevatorPositionCommand
import frc.team4069.robot.subsystems.ElevatorSubsystem

class DelayRunElevatorCommand(pos: ElevatorSubsystem.Position, timeout: Double) : CommandGroup() {
    init {
        addSequential(WaitCommand(timeout))
        addSequential(SetElevatorPositionCommand(pos))
    }
}
