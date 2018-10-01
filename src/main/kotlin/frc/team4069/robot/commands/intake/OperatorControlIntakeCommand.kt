package frc.team4069.robot.commands.intake

import edu.wpi.first.wpilibj.command.Command
import frc.team4069.robot.OI
import frc.team4069.robot.subsystems.IntakeSubsystem

class OperatorControlIntakeCommand : Command() {
    init {
        requires(IntakeSubsystem)
    }

    override fun execute() {
        val axis = OI.intakeAxis
//        val reverse = OI.controlJoystick.button(ButtonType.LEFT_STICK).value
        IntakeSubsystem.set(axis)
    }

    override fun isFinished(): Boolean {
        return false
    }
}
