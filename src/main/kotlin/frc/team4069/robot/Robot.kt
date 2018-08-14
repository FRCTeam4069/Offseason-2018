package frc.team4069.robot

import frc.team4069.robot.commands.drive.OperatorDriveCommand
import frc.team4069.robot.commands.elevator.OperatorControlElevatorCommand
import frc.team4069.robot.commands.intake.OperatorControlIntakeCommand
import frc.team4069.robot.subsystems.ArmSubsystem
import frc.team4069.robot.subsystems.DriveBaseSubsystem
import frc.team4069.robot.subsystems.ElevatorSubsystem
import frc.team4069.robot.subsystems.IntakeSubsystem
import frc.team4069.saturn.lib.SaturnRobot
import frc.team4069.saturn.lib.command.SubsystemHandler
import frc.team4069.saturn.lib.command.builders.parallel

class Robot : SaturnRobot() {

    override suspend fun initialize() {
        SubsystemHandler += ArmSubsystem
        SubsystemHandler += DriveBaseSubsystem
        SubsystemHandler += ElevatorSubsystem
        SubsystemHandler += IntakeSubsystem
    }

    override suspend fun teleoperatedInit() {
        parallel {
            +OperatorDriveCommand()
            +OperatorControlElevatorCommand()
            +OperatorControlIntakeCommand()
        }.start()
    }
}
