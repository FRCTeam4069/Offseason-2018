package frc.team4069.robot.commands.drive

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.command.Command
import frc.team4069.robot.Constants
import frc.team4069.robot.Localization
import frc.team4069.saturn.lib.math.Pose2d
import frc.team4069.saturn.lib.math.VelocityPIDFController
import frc.team4069.saturn.lib.math.uom.distance.DistanceUnit
import frc.team4069.saturn.lib.math.uom.velocity.VelocityUnit
import frc.team4069.saturn.lib.math.uom.velocity.fps
import kotlin.math.abs
import kotlin.math.sign
import kotlin.math.sin
import frc.team4069.robot.subsystems.DriveBaseSubsystem as driveBase

class DriveStraightCommand(val relativeDistance: DistanceUnit, val baseVelocity: VelocityUnit = 3.fps) : Command() {

    lateinit var initPose: Pose2d

    val leftPid = VelocityPIDFController(
//            p = Constants.DRIVETRAIN_P,
//            d = Constants.DRIVETRAIN_D,
            v = Constants.DRIVETRAIN_V,
            s = Constants.DRIVETRAIN_S,
            currentVelocity = { driveBase.leftVelocity.fps }
    )

    val rightPid = VelocityPIDFController(
//            p = Constants.DRIVETRAIN_P,
//            d = Constants.DRIVETRAIN_D,
            v = Constants.DRIVETRAIN_V,
            s = Constants.DRIVETRAIN_S,
            currentVelocity = { driveBase.rightVelocity.fps }
    )

    private val velocity = baseVelocity.fps * sign(relativeDistance.ft)

    init {
        requires(driveBase)
    }

    var checkY = false

    override fun initialize() {
        initPose = Localization.position
        checkY = when {
            sin(initPose.theta) > 0.5 -> true
            else -> false
        }
        Localization.reset()
    }

    override fun execute() {
        val lOut = leftPid.getPIDFOutput(velocity to 0.0)
        val rOut = rightPid.getPIDFOutput(velocity to 0.0)

        driveBase.set(ControlMode.PercentOutput,
                lOut,
                rOut)
    }

    override fun isFinished(): Boolean {
        val pose = Localization.position
        return if(checkY) {
            abs(pose.y) >= abs(relativeDistance.ft)
        }else {
            abs(pose.x) >= abs(relativeDistance.ft)
        }
    }

    override fun end() {
        driveBase.stop()
        val newPose = Localization.position
        val mergedPose = Pose2d(
                x = initPose.x + newPose.x,
                y = initPose.y + newPose.y,
                theta = initPose.theta + newPose.theta
        )

        Localization.reset(resetPose = mergedPose)
        println("Initial pose: $initPose")
        println("Terminating pose: $newPose")
        println("Final pose: $mergedPose")
    }
}
