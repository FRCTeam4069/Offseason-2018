package frc.team4069.robot.commands.drive

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.command.Command
import frc.team4069.robot.vision.VisionSystem
import frc.team4069.saturn.lib.math.uom.velocity.VelocityUnit
import frc.team4069.saturn.lib.math.uom.velocity.fps
import frc.team4069.robot.subsystems.DriveBaseSubsystem as driveBase

class FollowTapeCommand(val baseVelocity: VelocityUnit = 0.3.fps, val correctionFactor: Double = 1.0) : Command() {

    var lTraveledLast: Double = 0.0
    var rTraveledLast: Double = 0.0

    init {
        requires(driveBase)
    }

    override fun end() {
        driveBase.stop()
    }

    override fun execute() {
        val speedDelta = (VisionSystem.tapeX.toDouble() * correctionFactor) / VisionSystem.width.toDouble()
        val lCoef = 1 + speedDelta
        val rCoef = 1 - speedDelta
//        println("L:")
//        println(lCoef)
//        println("R:")
//        println(rCoef)
        val lDelta = driveBase.leftPosition.ft - lTraveledLast
        val rDelta = driveBase.rightPosition.ft - rTraveledLast
//        println(lDelta)
//        println(rDelta)
        println(driveBase.leftPosition.ft)
        println(driveBase.rightPosition.ft)
        val meanDelta = (lDelta + rDelta) / 2
        val lCoefActual = lDelta / meanDelta
        val rCoefActual = rDelta / meanDelta
        driveBase.set(ControlMode.PercentOutput, baseVelocity.fps * lCoef, baseVelocity.fps * rCoef)

        lTraveledLast = driveBase.leftPosition.ft
        lTraveledLast = driveBase.leftPosition.ft
    }

    override fun isFinished(): Boolean {
        return false
    }
}