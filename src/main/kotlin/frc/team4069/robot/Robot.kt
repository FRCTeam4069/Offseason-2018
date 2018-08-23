package frc.team4069.robot

import edu.wpi.first.wpilibj.ADXRS450_Gyro
import edu.wpi.first.wpilibj.SPI
import frc.team4069.robot.commands.drive.FollowPathCommand
import frc.team4069.robot.subsystems.ArmSubsystem
import frc.team4069.robot.subsystems.DriveBaseSubsystem
import frc.team4069.robot.subsystems.ElevatorSubsystem
import frc.team4069.robot.subsystems.IntakeSubsystem
import frc.team4069.saturn.lib.SaturnRobot
import jaci.pathfinder.Pathfinder
import jaci.pathfinder.Trajectory
import jaci.pathfinder.Waypoint

class Robot : SaturnRobot() {

    override suspend fun initialize() {
        +ArmSubsystem
        +DriveBaseSubsystem
        +ElevatorSubsystem
        +IntakeSubsystem
        +OI.controlJoystick
        +OI.driveJoystick
        Localization
    }

    override suspend fun autonomousInit() {
//        FollowPathCommand("straight-line.csv", true).start()
        val points = listOf(
                Waypoint(0.0, 0.0, 0.0),
                Waypoint(10.0, 0.0, 0.0)
        )
        val traj = Pathfinder.generate(points.toTypedArray(), Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_FAST, 0.02, 3.0, 3.0, 60.0))
        println("Trajectory is ${traj.length() * 0.02}s long")
        FollowPathCommand(traj, false)//.start()
    }

    companion object {
        val gyro by lazy {
            ADXRS450_Gyro(SPI.Port.kOnboardCS0)
        }
    }
}
