package frc.team4069.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.drive.DifferentialDrive
import frc.team4069.saturn.lib.command.Command
import frc.team4069.saturn.lib.command.Subsystem
import frc.team4069.saturn.lib.math.uom.distance.DistanceUnit
import frc.team4069.saturn.lib.math.uom.distance.NativeUnits
import frc.team4069.saturn.lib.math.uom.distance.preferences
import frc.team4069.saturn.lib.math.uom.velocity.FeetPerSecond
import frc.team4069.saturn.lib.math.uom.velocity.VelocityUnit
import frc.team4069.saturn.lib.motor.SaturnEncoder
import frc.team4069.saturn.lib.motor.SaturnSRX

object DriveBaseSubsystem : Subsystem() {
    val leftDrive = SaturnSRX(12, slaveIds = *intArrayOf(11, 13))
    val leftEncoder = SaturnEncoder(256, 0, 1)

    val rightDrive = SaturnSRX(19, slaveIds = *intArrayOf(18, 20))
    val rightEncoder = SaturnEncoder(256, 8, 9)


    val leftPosition: DistanceUnit
        get() = NativeUnits(leftEncoder.get(), preferences)

    val rightPosition: DistanceUnit
        get() = NativeUnits(leftEncoder.get(), preferences)

    val leftVelocity: VelocityUnit
        get() = FeetPerSecond(leftEncoder.rate, preferences)

    val rightVelocity: VelocityUnit
        get() = FeetPerSecond(rightEncoder.rate, preferences)

    private const val stopThreshold = DifferentialDrive.kDefaultQuickStopThreshold
    private const val stopAlpha = DifferentialDrive.kDefaultQuickStopAlpha

    override var defaultCommand: Command? = null

    private const val METRES_PER_ROTATION = 0.61

    private var stopAccumulator = 0.0

    init {
        leftEncoder.apply {
            setMaxPeriod(0.1)
            setMinRate(10.0)
            distancePerPulse = 1.0
            setReverseDirection(true)
            samplesToAverage = 7
        }

        rightEncoder.apply {
            setMaxPeriod(0.1)
            setMinRate(10.0)
            distancePerPulse = 1.0
            setReverseDirection(true)
            samplesToAverage = 7
        }


    }

    fun stop() {
        leftDrive.stop()
        rightDrive.stop()
    }

    fun reset() {
        stop()
        leftEncoder.reset()
        rightEncoder.reset()
    }

    /**
     * Drives using a drive algortihm with constant curvature given [turn], [speed], and [quickTurn]
     *
     * Alternate name is cheesyDrive
     */
    fun curvatureDrive(turn: Double, speed: Double, quickTurn: Boolean) {
        val angularPower: Double
        val overPower: Boolean

        if(quickTurn) {
            if (Math.abs(speed) < stopThreshold) {
                stopAccumulator = (1 - stopAlpha) * stopAccumulator + stopAlpha * turn.coerceIn(-1.0, 1.0) * 2.0
            }
            overPower = true
            angularPower = turn
        } else {
            overPower = false
            angularPower = Math.abs(speed) * turn - stopAccumulator

            when {
                stopAccumulator > 1 -> stopAccumulator -= 1.0
                stopAccumulator < -1 -> stopAccumulator += 1.0
                else -> stopAccumulator = 0.0
            }
        }

        var leftMotorOutput = speed + angularPower
        var rightMotorOutput = speed - angularPower

        // If rotationVector is overpowered, reduce both outputs to within acceptable range
        if (overPower) {
            when {
                leftMotorOutput > 1.0 -> {
                    rightMotorOutput -= leftMotorOutput - 1.0
                    leftMotorOutput = 1.0
                }
                rightMotorOutput > 1.0 -> {
                    leftMotorOutput -= rightMotorOutput - 1.0
                    rightMotorOutput = 1.0
                }
                leftMotorOutput < -1.0 -> {
                    rightMotorOutput -= leftMotorOutput + 1.0
                    leftMotorOutput = -1.0
                }
                rightMotorOutput < -1.0 -> {
                    leftMotorOutput -= rightMotorOutput + 1.0
                    rightMotorOutput = -1.0
                }
            }
        }

        set(ControlMode.PercentOutput, leftMotorOutput, rightMotorOutput)
    }

    fun set(mode: ControlMode, left: Double, right: Double) {
        leftDrive.set(mode, left)
        rightDrive.set(mode, right)
    }

    val distanceTraveledMetres: Double
        get() {
            val leftWheelRotations = Math.abs(leftEncoder.distanceTraveledRotations)
            val rightWheelRotations = Math.abs(rightEncoder.distanceTraveledRotations)

            val average = (leftWheelRotations + rightWheelRotations) / 2

            return average * METRES_PER_ROTATION
        }
}