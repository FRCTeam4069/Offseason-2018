package frc.team4069.robot.commands.drive

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.command.Command
import frc.team4069.robot.Constants
import frc.team4069.robot.vision.VisionSystem
import frc.team4069.saturn.lib.math.VelocityPIDFController
import frc.team4069.saturn.lib.math.uom.velocity.VelocityUnit
import frc.team4069.saturn.lib.math.uom.velocity.fps
import frc.team4069.robot.subsystems.DriveBaseSubsystem as driveBase

class FollowTapeCommand(val baseVelocity: VelocityUnit = 0.25.fps, val correctionFactor: Double = 0.75) : Command() {

    val leftPid = VelocityPIDFController(
            p = Constants.DRIVETRAIN_P,
            d = Constants.DRIVETRAIN_D,
            v = Constants.DRIVETRAIN_V,
            s = Constants.DRIVETRAIN_S,
            currentVelocity = { driveBase.leftVelocity.fps }
    )

    val rightPid = VelocityPIDFController(
            p = Constants.DRIVETRAIN_P,
            d = Constants.DRIVETRAIN_D,
            v = Constants.DRIVETRAIN_V,
            s = Constants.DRIVETRAIN_S,
            currentVelocity = { driveBase.rightVelocity.fps }
    )

    init {
        requires(driveBase)
    }

    override fun end() {
        driveBase.stop()
    }

    override fun execute() {
        val speedDelta = (VisionSystem.tapeX.toDouble() * correctionFactor) / VisionSystem.width.toDouble()
        val lOut = leftPid.getPIDFOutput(baseVelocity.fps + speedDelta, 0.0)
        val rOut = rightPid.getPIDFOutput(baseVelocity.fps - speedDelta, 0.0)
        println("L:")
        println(lOut)
        println("R:")
        println(rOut)
        driveBase.set(ControlMode.PercentOutput, lOut, rOut)
    }

    override fun isFinished(): Boolean {
        return false
    }
}