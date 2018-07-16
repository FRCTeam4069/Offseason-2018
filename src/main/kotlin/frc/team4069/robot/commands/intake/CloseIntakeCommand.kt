package frc.team4069.robot.commands.intake

import frc.team4069.saturn.lib.command.InstantCommand
import frc.team4069.robot.subsystems.IntakeSubsystem as intake

class CloseIntakeCommand : InstantCommand() {
    init {
        requires(intake)
    }

    override fun onCreate() {
        intake.grab()
    }
}