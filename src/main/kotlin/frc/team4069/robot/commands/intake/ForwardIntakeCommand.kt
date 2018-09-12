package frc.team4069.robot.commands.intake

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.command.Command
import frc.team4069.robot.Localization
import frc.team4069.saturn.lib.math.uom.distance.DistanceUnit
import frc.team4069.saturn.lib.math.uom.distance.ft
import kotlin.math.sin
import frc.team4069.robot.subsystems.DriveBaseSubsystem as driveBase
import frc.team4069.robot.subsystems.IntakeSubsystem as intake

class ForwardIntakeCommand(forwardThreshold: DistanceUnit = 6.ft) : Command() {

    var checkY = false
    private val threshold: Double
    val initPose = Localization.position

    lateinit var finalDist: Double

    init {
        requires(intake)
        requires(driveBase)


        checkY = when {
            sin(initPose.theta) > 0.5 -> true
            else -> false
        }

        threshold = if(checkY) {
            initPose.y + forwardThreshold.ft
        }else {
            initPose.x + forwardThreshold.ft
        }

    }

    override fun initialize() {
        driveBase.set(ControlMode.PercentOutput, 0.3, 0.3)
        intake.set(1.0, false)
    }

    override fun end() {
        val pose = Localization.position
        finalDist = if(checkY) {
            pose.y - initPose.y
        }else {
            pose.x - initPose.x
        }
    }

    override fun isFinished(): Boolean {
        val pose = Localization.position
        var outCondition = intake.outputCurrent >= 20.0
//        outCondition = if(checkY) {
//            outCondition || pose.y >= threshold
//        }else {
//            outCondition || pose.x >= threshold
//        }
        return outCondition
    }
}