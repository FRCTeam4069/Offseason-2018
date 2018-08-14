package frc.team4069.robot.commands.intake

import frc.team4069.robot.OI
import frc.team4069.robot.subsystems.IntakeSubsystem
import frc.team4069.saturn.lib.command.Command

class OperatorControlIntakeCommand : Command() {
    init {
        +IntakeSubsystem
    }

    override suspend fun execute() {
        val axis = OI.intakeAxis
        IntakeSubsystem.set(axis)
    }
}
