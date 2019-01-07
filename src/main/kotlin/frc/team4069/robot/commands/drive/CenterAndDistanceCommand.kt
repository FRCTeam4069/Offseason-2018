package frc.team4069.robot.commands.drive

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.command.Command
import frc.team4069.robot.vision.VisionSystem
import frc.team4069.saturn.lib.math.uom.velocity.VelocityUnit
import frc.team4069.saturn.lib.math.uom.velocity.fps
import frc.team4069.robot.subsystems.DriveBaseSubsystem as driveBase

class CenterAndDistanceCommand(val baseVelocity: VelocityUnit = 0.3.fps, val tapeP: Double = 0.75, val posP: Double = 2.0) : Command() {

    var lTraveledLast: Double = 0.0
    var rTraveledLast: Double = 0.0

    init {
        requires(driveBase)
    }

    override fun end() {
        driveBase.stop()
    }

    override fun execute() {
        val speedDelta = (VisionSystem.tapeX.toDouble() * tapeP) / VisionSystem.width.toDouble()
        val expectedLCoef = 1 + speedDelta
        val expectedRCoef = 1 - speedDelta
        val lDelta = driveBase.leftPosition.ft - lTraveledLast
        val rDelta = driveBase.rightPosition.ft - rTraveledLast
        val meanDelta = (lDelta + rDelta) / 2
        val outputLCoef: Double
        val outputRCoef: Double
        if (meanDelta == 0.0) {
            outputLCoef = expectedLCoef
            outputRCoef = expectedRCoef
        } else {
            val measuredLCoef = lDelta / meanDelta
            val measuredRCoef = rDelta / meanDelta
            val lCoefError = measuredLCoef - expectedLCoef
            val rCoefError = measuredRCoef - expectedRCoef
            outputLCoef = expectedLCoef - (lCoefError * posP)
            outputRCoef = expectedRCoef - (rCoefError * posP)
        }
        val speed = (driveBase.leftVelocity.fps + driveBase.rightVelocity.fps) / 2.0
        var vel = (0.6 * (VisionSystem.distance - 2.0)) - (speed * 0.3)
        if (vel > 0.25) {
            vel = 0.25
        }
        if (vel < -0.25) {
            vel = -0.25
        }

        driveBase.set(ControlMode.PercentOutput, vel * outputLCoef, vel * outputRCoef)

        lTraveledLast = driveBase.leftPosition.ft
        rTraveledLast = driveBase.rightPosition.ft
    }

    override fun isFinished(): Boolean {
//        return (VisionSystem.distance < 2.0)
        return false
    }
}