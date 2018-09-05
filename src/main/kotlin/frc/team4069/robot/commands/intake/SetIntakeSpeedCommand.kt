package frc.team4069.robot.commands.intake

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team4069.robot.subsystems.IntakeSubsystem

class SetIntakeSpeedCommand(private val speed: Double) : InstantCommand() {

    init {
        requires(IntakeSubsystem)
    }

    override fun initialize() {
        IntakeSubsystem.set(speed, false)
    }

    override fun end() {
        IntakeSubsystem.stop()
    }
}