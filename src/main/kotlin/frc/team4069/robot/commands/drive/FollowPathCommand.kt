package frc.team4069.robot.commands.drive

import com.ctre.phoenix.motorcontrol.ControlMode
import frc.team4069.robot.Localization
import frc.team4069.saturn.lib.command.Command
import frc.team4069.saturn.lib.command.condition
import frc.team4069.saturn.lib.math.Pose2d
import frc.team4069.saturn.lib.math.RamsyeetPathFollower
import frc.team4069.saturn.lib.math.VelocityPIDFController
import frc.team4069.saturn.lib.math.uom.distance.ft
import jaci.pathfinder.Pathfinder
import jaci.pathfinder.Trajectory
import java.io.File
import frc.team4069.robot.subsystems.DriveBaseSubsystem as driveBase

class FollowPathCommand(path: Trajectory, zeroPose: Boolean) : Command() {
    private val follower = RamsyeetPathFollower(path, 0.8, 0.75)

    private var lastVelocity = 0.0 to 0.0
    private val dt = path.segments[0].dt

    private val lController = VelocityPIDFController(
            p = 0.4,
            i = 0.0,
            d = 0.2,
            //v = 1/14.0, //TODO: 1/max_velocity
            a = 0.0,
            s = 0.0,
            currentVelocity = { driveBase.leftVelocity.fps }
    )

    private val rController = VelocityPIDFController(
            p = 0.4,
            i = 0.0,
            d = 0.2,
            //v = 1/14.0, // TODO: 1/max_velocity
            a = 0.0,
            s = 0.0,
            currentVelocity = { driveBase.rightVelocity.fps }
    )

    init {
        println("Ramsete starting")
        +driveBase
        val firstSegment = path[0]
        if(zeroPose) {
            Localization.reset(Pose2d(firstSegment.x, firstSegment.y, 0.0))
            println("Pos: ${Localization.position}")
        }

        // 1 second / dt (ms) = freq (hz)

        println(follower.isFinished)
        finishCondition += condition { follower.isFinished }
    }

    override suspend fun execute() {
        val currentPose = Localization.position

        val twist = follower.update(currentPose)
        val output = twist.inverseKinematics(1.791.ft) //TODO: Real drive base width
        val (left, right) = output

        driveBase.set(ControlMode.PercentOutput,
                lController.update(left to (left - lastVelocity.first) / dt),
                rController.update(right to (right - lastVelocity.second) / dt))

        println("LHS error: ${lController.lastError}")
        println("RHS error: ${rController.lastError}")

        lastVelocity = left to right
    }

    override suspend fun dispose() {
        driveBase.stop()
    }

    constructor(csvName: String, zeroPose: Boolean) : this(Pathfinder.readFromCSV(File("/home/lvuser/paths/$csvName")), zeroPose)
}