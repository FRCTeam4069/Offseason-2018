package frc.team4069.robot

import edu.wpi.first.wpilibj.ADXRS450_Gyro
import edu.wpi.first.wpilibj.SPI
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
        OI
        Localization
        stateMachine.onWhile(State.TELEOP) {
            OI.controlJoystick.update()
            OI.driveJoystick.update()
        }
    }

    override suspend fun teleoperatedInit() {
        parallel {
            +OperatorDriveCommand()
            +OperatorControlElevatorCommand()
            +OperatorControlIntakeCommand()
        }.start()
    }

//    override suspend fun autonomousInit() {
//        FollowPathCommand("better-robot-test.csv", true).start()
//    }

    companion object {
        val gyro by lazy {
            ADXRS450_Gyro(SPI.Port.kOnboardCS0)
        }
    }
}
