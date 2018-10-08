package frc.team4069.robot.commands.intake

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team4069.robot.subsystems.IntakeSubsystem

class ToggleOpenIntakeCommand : InstantCommand() {
    init {
        requires(IntakeSubsystem)
    }

    override fun initialize() {
        IntakeSubsystem.toggleSolenoid()
    }
}