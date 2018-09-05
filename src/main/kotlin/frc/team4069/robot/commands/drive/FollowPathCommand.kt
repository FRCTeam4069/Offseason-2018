package frc.team4069.robot.commands.drive

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.command.Command
import frc.team4069.robot.Localization
import frc.team4069.saturn.lib.math.Pose2d
import frc.team4069.saturn.lib.math.RamsyeetPathFollower
import frc.team4069.saturn.lib.math.VelocityPIDFController
import frc.team4069.saturn.lib.math.uom.distance.ft
import jaci.pathfinder.Pathfinder
import jaci.pathfinder.Trajectory
import java.io.File
import frc.team4069.robot.subsystems.DriveBaseSubsystem as driveBase

class FollowPathCommand(val path: Trajectory, zeroPose: Boolean) : Command() {
    private val follower = RamsyeetPathFollower(path, 0.8, 0.75)

    private var lastVelocity = 0.0 to 0.0
    val dt = path[0].dt

    private val lController = VelocityPIDFController(
            p = 0.2,
//            i = 0.0,
//            d = 0.05,
            v = 0.07143,
            a = 0.0,
            s = 0.1,
            currentVelocity = { driveBase.leftVelocity.fps }
    )

    private val rController = VelocityPIDFController(
            p = 0.2,
//            i = 0.0,
//            d = 0.05,
            v = 0.07143,
            a = 0.0,
            s = 0.1,
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

        // 1 second / dt (ms) = freq (hz)
//        updateFrequency = (1 / dt).toInt()

        println("Path is ${path.length()} segments long")
//        finishCondition += condition { segmentIdx >= path.length() - 1 }
    }

    override fun initialize() {
        driveBase.reset()
    }

    override fun execute() {

        val currentPose = Localization.position

        val twist = follower.update(currentPose)
        val output = twist.inverseKinematics(1.791.ft)
        val (left, right) = output

        val leftOut = lController.getPIDFOutput(left to (left - lastVelocity.first) / dt)
        val rightOut = rController.getPIDFOutput(right to (right - lastVelocity.second) / dt)

        println("PID out left: $leftOut. PID out right: $rightOut")

        driveBase.set(ControlMode.PercentOutput,
                leftOut,
                rightOut)
        lastVelocity = left to right
    }

    override fun end() {
        driveBase.stop()
    }

    override fun isFinished(): Boolean {
        return follower.isFinished
    }

    constructor(csvName: String, zeroPose: Boolean) : this(Pathfinder.readFromCSV(File("/home/lvuser/paths/$csvName")), zeroPose)
}