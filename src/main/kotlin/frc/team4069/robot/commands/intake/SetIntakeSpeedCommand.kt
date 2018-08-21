package frc.team4069.robot.commands.intake

import frc.team4069.robot.subsystems.IntakeSubsystem
import frc.team4069.saturn.lib.command.Command

class SetIntakeSpeedCommand(private val speed: Double) : Command() {

    init {
        +IntakeSubsystem
    }

    override suspend fun initialize() {
        IntakeSubsystem.set(speed, false)
    }

    override suspend fun dispose() {
        IntakeSubsystem.stop()
    }
}