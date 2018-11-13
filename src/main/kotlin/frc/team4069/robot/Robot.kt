package frc.team4069.robot

import edu.wpi.first.wpilibj.ADXRS450_Gyro
import edu.wpi.first.wpilibj.SPI
import edu.wpi.first.wpilibj.command.Scheduler
import edu.wpi.first.wpilibj.livewindow.LiveWindow
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team4069.robot.commands.OperatorControlCommandGroup
import frc.team4069.robot.commands.drive.FollowPathCommand
import frc.team4069.robot.subsystems.ArmSubsystem
import frc.team4069.robot.subsystems.DriveBaseSubsystem
import frc.team4069.robot.subsystems.ElevatorSubsystem
import frc.team4069.robot.subsystems.IntakeSubsystem
import frc.team4069.saturn.lib.SaturnRobot

class Robot : SaturnRobot() {
    override fun robotInit() {
        LiveWindow.disableAllTelemetry()
        Localization
        NTConnection
        NetworkInterface
        Trajectories


        +OI.driveJoystick
        +OI.controlJoystick

        SmartDashboard.putBoolean("Voltage Nominal", true)

//         Subsystem initialization
        DriveBaseSubsystem
        ArmSubsystem
        ElevatorSubsystem
        IntakeSubsystem

        DriveBaseSubsystem.reset()
    }

    override fun teleopInit() {
        Scheduler.getInstance().add(OperatorControlCommandGroup())
    }

    override fun autonomousInit() {
        Scheduler.getInstance().add(FollowPathCommand(Trajectories.testCurvature, zeroPose = true))
    }

    override fun disabledInit() {
        IntakeSubsystem.disableSolenoid()
    }

    override fun notifyBrownout() {
        DriveBaseSubsystem.reduceLimits()
        ElevatorSubsystem.reduceLimits()

        SmartDashboard.putBoolean("Voltage Nominal", false)
    }

    companion object {
        val gyro by lazy {
            ADXRS450_Gyro(SPI.Port.kOnboardCS0)
        }
    }
}
