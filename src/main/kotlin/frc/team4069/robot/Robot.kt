package frc.team4069.robot

import edu.wpi.first.wpilibj.ADXRS450_Gyro
import edu.wpi.first.wpilibj.IterativeRobot
import edu.wpi.first.wpilibj.SPI
import edu.wpi.first.wpilibj.command.Scheduler
import frc.team4069.robot.commands.OperatorControlCommandGroup
import frc.team4069.robot.commands.drive.FollowPathCommand
import frc.team4069.robot.subsystems.DriveBaseSubsystem

//class Robot : SaturnRobot(true) {
class Robot : IterativeRobot() {

//    override suspend fun initialize() {
//        +ArmSubsystem
//        +DriveBaseSubsystem
//        +ElevatorSubsystem
//        +IntakeSubsystem
//        +OI.controlJoystick
//        +OI.driveJoystick
//        Localization
//    }

    override fun robotInit() {
        Localization
        NTConnection
        NetworkInterface

        DriveBaseSubsystem.reset()
    }

    override fun robotPeriodic() {
        Scheduler.getInstance().run()
    }

    override fun autonomousInit() {
        Scheduler.getInstance().add(FollowPathCommand("close-scale-5fps.csv", true))//.start()
//        Scheduler.getInstance().add(AutoCommandGroup())
    }

    override fun teleopInit() {
        Scheduler.getInstance().add(OperatorControlCommandGroup())
    }

    companion object {
        val gyro by lazy {
            ADXRS450_Gyro(SPI.Port.kOnboardCS0)
        }
    }
}
