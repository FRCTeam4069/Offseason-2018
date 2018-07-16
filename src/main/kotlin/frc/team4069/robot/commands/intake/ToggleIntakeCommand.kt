package frc.team4069.robot.commands.intake

import frc.team4069.saturn.lib.command.ConditionalCommand
import frc.team4069.robot.subsystems.IntakeSubsystem as intake

class ToggleIntakeCommand : ConditionalCommand(
        onTrue = OpenIntakeCommand(),
        onFalse = CloseIntakeCommand()) {
    override val condition: Boolean
        get() = intake.isClosed
}
