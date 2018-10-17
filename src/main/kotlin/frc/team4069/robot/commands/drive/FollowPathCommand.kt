package frc.team4069.robot.commands.drive

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.command.Command
import frc.team4069.robot.Constants
import frc.team4069.robot.Localization
import frc.team4069.saturn.lib.math.Pose2d
import frc.team4069.saturn.lib.math.RamsyeetPathFollower
import frc.team4069.saturn.lib.math.VelocityPIDFController
import frc.team4069.saturn.lib.math.uom.distance.ft
import jaci.pathfinder.Pathfinder
import jaci.pathfinder.Trajectory
import java.io.File
import frc.team4069.robot.subsystems.DriveBaseSubsystem as driveBase

class FollowPathCommand(path: Trajectory, zeroPose: Boolean, reversed: Boolean = false) : Command() {
//    private val follower = RamsyeetPathFollower(path, Constants.kZeta, Constants.kB)
    private val follower: RamsyeetPathFollower

    private var lastVelocity = 0.0 to 0.0
    val dt = path[0].dt

    val sign = if(reversed) -1 else 1

    private val lController = VelocityPIDFController(
            p = Constants.DRIVETRAIN_P,
            d = Constants.DRIVETRAIN_D,
            v = Constants.DRIVETRAIN_V,
            s = Constants.DRIVETRAIN_S,
            currentVelocity = { driveBase.leftVelocity.fps }
    )

    private val rController = VelocityPIDFController(
            p = Constants.DRIVETRAIN_P,
            d = Constants.DRIVETRAIN_D,
            v = Constants.DRIVETRAIN_V,
            s = Constants.DRIVETRAIN_S,
            currentVelocity = { driveBase.rightVelocity.fps }
    )

    init {
        println("Ramsete starting")
        requires(driveBase)
        val firstSegment = path[0]
        if (zeroPose) {
            Localization.reset(Pose2d(firstSegment.x, firstSegment.y, 0.0))
            println("Pos: ${Localization.position}")
        }

        follower = if(reversed) {
            val dist = path.segments.last().position
            val newPath = Trajectory(path.segments.reversed()
                    .map {
                        Trajectory.Segment(
                                it.dt,
                                it.x,
                                it.y,
                                dist - it.position,
                                -it.velocity,
                                -it.acceleration,
                                -it.jerk,
                                it.heading
                        )
                    }.toTypedArray())
            RamsyeetPathFollower(newPath, Constants.kZeta, Constants.kB)
        }else {
            RamsyeetPathFollower(path, Constants.kZeta, Constants.kB)
        }

        println("Path is ${path.length() * dt} seconds long")
    }

    override fun initialize() {
        driveBase.stop()
    }

    override fun execute() {
        val currentPose = Localization.position

        val twist = follower.update(currentPose)
        val (left, right) = twist.inverseKinematics(Constants.DRIVETRAIN_WIDTH_FT.ft)

        val leftOut = lController.getPIDFOutput(left, (left - lastVelocity.first) / dt)
        val rightOut = rController.getPIDFOutput(right, (right - lastVelocity.second) / dt)
//
//        println("PID out left: $leftOut. PID out right: $rightOut")

        updateDashboard()

        driveBase.set(ControlMode.PercentOutput,
                leftOut,
                rightOut)
        lastVelocity = left to right
    }

    override fun end() {
        driveBase.stop()
        println("Ending pose is ${Localization.position}")
    }

    override fun isFinished(): Boolean {
        return follower.isFinished
    }

    constructor(csvName: String, zeroPose: Boolean, reversed: Boolean = false) : this(Pathfinder.readFromCSV(File("/home/lvuser/paths/$csvName")), zeroPose, reversed)

    private fun updateDashboard() {
        val seg = follower.getCurrentSegment()
        if(seg != null) {
            pathX = seg.x
            pathY = seg.y
            pathHdg = seg.heading
        }
    }

    companion object {
        var pathX = 0.0
            private set

        var pathY = 0.0
            private set

        var pathHdg = 0.0
            private set
    }
}