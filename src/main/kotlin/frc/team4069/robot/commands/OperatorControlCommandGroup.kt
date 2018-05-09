package frc.team4069.robot.commands

import frc.team4069.robot.commands.drive.OperatorDriveCommand
import frc.team4069.robot.commands.elevator.OperatorControlElevatorCommand
import frc.team4069.robot.commands.intake.OperatorControlIntakeCommand
import frc.team4069.saturn.lib.command.CommandGroup

class OperatorControlCommandGroup : CommandGroup() {
    init {
        addParallel(OperatorDriveCommand())
        addParallel(OperatorControlElevatorCommand())
        addParallel(OperatorControlIntakeCommand())
    }
}