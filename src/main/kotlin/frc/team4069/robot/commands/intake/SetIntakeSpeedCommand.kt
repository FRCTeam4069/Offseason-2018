package frc.team4069.robot.commands.intake

import frc.team4069.saturn.lib.command.InstantCommand
import frc.team4069.robot.subsystems.IntakeSubsystem as intake

class SetIntakeSpeedCommand(private val speed: Double) : InstantCommand() {

    init {
        requires(intake)
    }

    override fun onCreate() {
        intake.set(speed)
    }
}