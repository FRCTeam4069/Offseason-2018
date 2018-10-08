package frc.team4069.robot.commands.intake

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.command.Command
import frc.team4069.saturn.lib.math.uom.distance.DistanceUnit
import frc.team4069.saturn.lib.math.uom.distance.ft
import kotlin.math.abs
import frc.team4069.robot.subsystems.DriveBaseSubsystem as driveBase
import frc.team4069.robot.subsystems.IntakeSubsystem as intake

class ForwardIntakeCommand(val forwardThreshold: DistanceUnit = 6.ft) : Command() {


    var init: Double = Double.NaN
    var threshold: Double = Double.NaN

    var finalDist: Double = Double.NaN

    init {
        requires(intake)
        requires(driveBase)

    }

    override fun initialize() {

        init = (driveBase.leftPosition.ft + driveBase.rightPosition.ft) / 2
        threshold = init + forwardThreshold.ft

        driveBase.set(ControlMode.PercentOutput, 0.3, 0.3)
        intake.set(1.0)
    }

    override fun end() {
        driveBase.stop()
        val pos = (driveBase.leftPosition.ft + driveBase.rightPosition.ft) / 2
        println("init is $init. pos is $pos")
        finalDist = pos - init
    }

    override fun isFinished(): Boolean {
        val pose = (driveBase.leftPosition.ft + driveBase.rightPosition.ft) / 2
        val absPos = abs(pose - init)

        return intake.outputCurrent >= 15.0 || absPos >= abs(forwardThreshold.ft)
//        outCondition = if(checkY) {
//            outCondition || pose.y >= threshold
//        }else {
//            outCondition || pose.x >= threshold
//        }
    }
}