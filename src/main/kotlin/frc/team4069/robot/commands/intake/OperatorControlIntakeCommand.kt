package frc.team4069.robot.commands.intake

import frc.team4069.robot.OI
import frc.team4069.saturn.lib.command.Command
import frc.team4069.robot.subsystems.IntakeSubsystem as intake

class OperatorControlIntakeCommand : Command() {
    init {
        requires(intake)
    }

    override fun periodic() {
        val speed = OI.intakeSpeedAxis
        intake.set(speed)
    }

    override val isFinished = false
}
