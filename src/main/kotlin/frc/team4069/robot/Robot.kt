package frc.team4069.robot

import edu.wpi.first.wpilibj.ADXRS450_Gyro
import edu.wpi.first.wpilibj.SPI
import edu.wpi.first.wpilibj.command.Scheduler
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
        Localization
        NTConnection
        NetworkInterface

        // Subsystem initialization
        DriveBaseSubsystem
        ArmSubsystem
        ElevatorSubsystem
        IntakeSubsystem

        SmartDashboard.putNumber("POV angle", -1.0)

        DriveBaseSubsystem.reset()
    }

    override fun robotPeriodic() {
        Scheduler.getInstance().run()
    }

    override fun autonomousInit() {
        Scheduler.getInstance().add(FollowPathCommand("switch-right.csv", true))//.start()
    }

    override fun teleopInit() {
        Scheduler.getInstance().add(OperatorControlCommandGroup())
    }

    override fun disabledInit() {
        NetworkInterface.stopTracking()
    }

    companion object {
        val gyro by lazy {
            ADXRS450_Gyro(SPI.Port.kOnboardCS0)
        }
    }
}
