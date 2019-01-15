package frc.team4069.robot.commands.intake

import edu.wpi.first.wpilibj.command.Command
import frc.team4069.robot.OI
import frc.team4069.robot.subsystems.IntakeSubsystem
import frc.team4069.saturn.lib.commands.SaturnCommand

class OperatorControlIntakeCommand : SaturnCommand(IntakeSubsystem) {

    override suspend fun execute() {
        val axis = OI.intakeAxis
//        val reverse = OI.controlJoystick.control(ButtonType.LEFT_STICK).value
        IntakeSubsystem.set(axis)
    }
}
