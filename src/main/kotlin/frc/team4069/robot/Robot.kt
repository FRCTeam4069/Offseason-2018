package frc.team4069.robot

import edu.wpi.first.wpilibj.ADXRS450_Gyro
import edu.wpi.first.wpilibj.SPI
import edu.wpi.first.wpilibj.command.Scheduler
import edu.wpi.first.wpilibj.livewindow.LiveWindow
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team4069.robot.commands.AutoCommandGroup
import frc.team4069.robot.commands.OperatorControlCommandGroup
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

        // Subsystem initialization
        DriveBaseSubsystem
        ArmSubsystem
        ElevatorSubsystem
        IntakeSubsystem

        SmartDashboard.putNumber("POV angle", -1.0)
        SmartDashboard.putBoolean("Over 30", false)

        DriveBaseSubsystem.reset()
    }

    override fun autonomousInit() {
//        Scheduler.getInstance().add(FollowPathCommand("switch-right.csv", true))//.start()
//        Scheduler.getInstance().add(FollowPathCommand("switch-right.csv", true))
        Scheduler.getInstance().add(AutoCommandGroup())
        Pneumatics.enable()
    }

    override fun teleopInit() {
        Scheduler.getInstance().add(OperatorControlCommandGroup())
        Pneumatics.enable()
    }

    override fun testInit() {
        Pneumatics.enable()
    }

    override fun disabledInit() {
        IntakeSubsystem.disableSolenoid()
//        NetworkInterface.stopTracking()
    }

    override fun notifyBrownout() {
        Pneumatics.disable()

        DriveBaseSubsystem.reduceLimits()
        ElevatorSubsystem.reduceLimits()

        SmartDashboard.putBoolean("Brownout Occurred", true)
    }

    companion object {
        val gyro by lazy {
            ADXRS450_Gyro(SPI.Port.kOnboardCS0)
        }
    }
}
