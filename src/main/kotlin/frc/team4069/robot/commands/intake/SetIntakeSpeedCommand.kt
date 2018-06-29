package frc.team4069.robot.commands.intake

import frc.team4069.robot.subsystems.IntakeSubsystem
import frc.team4069.saturn.lib.command.Command

class SetIntakeSpeedCommand(private val speed: Double) : Command() {

    init {
        requires(IntakeSubsystem)
    }

    override fun onCreate() {
        IntakeSubsystem.set(speed)
    }

    override val isFinished = true
}