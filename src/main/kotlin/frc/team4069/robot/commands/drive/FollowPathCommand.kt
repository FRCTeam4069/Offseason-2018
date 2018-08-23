package frc.team4069.robot.commands.drive

import com.ctre.phoenix.motorcontrol.ControlMode
import frc.team4069.robot.Localization
import frc.team4069.saturn.lib.command.Command
import frc.team4069.saturn.lib.command.condition
import frc.team4069.saturn.lib.math.*
import frc.team4069.saturn.lib.math.uom.distance.ft
import jaci.pathfinder.Pathfinder
import jaci.pathfinder.Trajectory
import kotlinx.coroutines.experimental.delay
import java.io.File
import java.util.concurrent.TimeUnit
import frc.team4069.robot.subsystems.DriveBaseSubsystem as driveBase

class FollowPathCommand(val path: Trajectory, zeroPose: Boolean) : Command() {
    private val follower = RamsyeetPathFollower(path, 0.8, 0.75)

    private var lastVelocity = 0.0 to 0.0
    private val dt = path[0].dt

    private val lController = VelocityPIDFController(
//            p = 0.6,
//            i = 0.0,
//            d = 0.1,
            v = 0.07143,
            a = 0.0,
            s = 0.1,
            currentVelocity = { driveBase.leftVelocity.fps }
    )

    private val rController = VelocityPIDFController(
//            p = 0.6,
//            i = 0.0,
//            d = 0.1,
            v = 0.07143,
            a = 0.0,
            s = 0.1,
            currentVelocity = { driveBase.rightVelocity.fps }
    )

    init {
        println("Ramsete starting")
        +driveBase
        val firstSegment = path[0]
        if (zeroPose) {
            Localization.reset(Pose2d(firstSegment.x, firstSegment.y, 0.0))
            println("Pos: ${Localization.position}")
        }

        // 1 second / dt (ms) = freq (hz)
        updateFrequency = (1 / dt).toInt()

        println("Path is ${path.length()} segments long")
        finishCondition += condition { segmentIdx >= path.length() - 1 }
    }

    var lastTheta = 0.0
    var segmentIdx = 0

    private var lastTime = -1L

    override suspend fun execute() {

        val time = System.nanoTime()
        if(lastTime > 0) {
            val commandDt = time - lastTime
            val diff = (TimeUnit.SECONDS.toNanos(1) / updateFrequency) - commandDt
            if(!(diff epsilonEquals 0L)) {
                delay(diff)
            }
        }
        lastTime = time

//        val currentPose = Localization.position

        // Temporary measure to try to get the robot to follow the path without correction
        val segment = path[segmentIdx++]

        val v = segment.velocity
        val w = if (segmentIdx >= path.length() - 1) {
            0.0
        } else {
            (segment.heading - lastTheta) / dt
        }

        val (left, right) = Twist2d(v, w).inverseKinematics(1.791.ft)

//        val twist = follower.update(currentPose)
//        val output = twist.inverseKinematics(1.791.ft)
//        val (left, right) = output

        val leftOut = lController.getPIDFOutput(left to (left - lastVelocity.first) / dt)
        val rightOut = rController.getPIDFOutput(right to (right - lastVelocity.second) / dt)

        println("PID out left: $leftOut. PID out right: $rightOut")

        driveBase.set(ControlMode.PercentOutput,
                leftOut,
                rightOut)
        if (segmentIdx > path.length() - 1) {
            driveBase.stop()
            return
        }
        lastVelocity = left to right
    }

    override suspend fun dispose() {
        println("Command getting disposed. idx at $segmentIdx")
        driveBase.stop()
    }

    constructor(csvName: String, zeroPose: Boolean) : this(Pathfinder.readFromCSV(File("/home/lvuser/paths/$csvName")), zeroPose)
}