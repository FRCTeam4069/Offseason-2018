package frc.team4069.robot.commands.drive

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.command.Command
import frc.team4069.robot.Constants
import frc.team4069.saturn.lib.math.VelocityPIDFController
import frc.team4069.saturn.lib.math.uom.distance.DistanceUnit
import frc.team4069.saturn.lib.math.uom.velocity.VelocityUnit
import frc.team4069.saturn.lib.math.uom.velocity.fps
import kotlin.math.abs
import kotlin.math.sign
import frc.team4069.robot.subsystems.DriveBaseSubsystem as driveBase

class DriveStraightCommand(val relativeDistance: () -> DistanceUnit, val baseVelocity: VelocityUnit = 3.fps) : Command() {

    constructor(dist: DistanceUnit, baseVelocity: VelocityUnit = 3.fps) : this({ dist }, baseVelocity)

    var init = Double.NaN

    lateinit var dist: DistanceUnit

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

    private var velocity = Double.NaN

    init {
        requires(driveBase)
    }

    override fun initialize() {
        init = (driveBase.leftPosition.ft + driveBase.rightPosition.ft) / 2
        dist = relativeDistance()
        velocity = baseVelocity.fps * sign(dist.ft)
    }

    override fun end() {
        driveBase.stop()
    }

    override fun execute() {
        val lOut = leftPid.getPIDFOutput(velocity, 0.0)
        val rOut = rightPid.getPIDFOutput(velocity, 0.0)

        driveBase.set(
            ControlMode.PercentOutput,
                lOut,
                rOut)
    }

    override fun isFinished(): Boolean {
        val pos = (driveBase.leftPosition.ft + driveBase.rightPosition.ft) / 2
        val absPos = abs(pos - init)
        return absPos >= abs(dist.ft)
    }
}
