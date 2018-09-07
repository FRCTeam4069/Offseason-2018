package frc.team4069.robot

import edu.wpi.first.wpilibj.ADXRS450_Gyro
import edu.wpi.first.wpilibj.IterativeRobot
import edu.wpi.first.wpilibj.SPI
import edu.wpi.first.wpilibj.command.Scheduler
import frc.team4069.robot.commands.AutoCommandGroup

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
    }

    override fun robotPeriodic() {
        Scheduler.getInstance().run()
    }

    override fun autonomousInit() {
//        Scheduler.getInstance().add(FollowPathCommand("switch-right.csv", true))//.start()
        Scheduler.getInstance().add(AutoCommandGroup())
    }

    companion object {
        val gyro by lazy {
            ADXRS450_Gyro(SPI.Port.kOnboardCS0)
        }
    }
}
