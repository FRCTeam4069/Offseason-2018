package frc.team4069.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.command.Subsystem
import edu.wpi.first.wpilibj.drive.DifferentialDrive
import frc.team4069.robot.RobotMap
import frc.team4069.robot.commands.drive.OperatorDriveCommand
import frc.team4069.saturn.lib.mathematics.units.Length
import frc.team4069.saturn.lib.mathematics.units.derivedunits.Velocity
import frc.team4069.saturn.lib.mathematics.units.derivedunits.velocity
import frc.team4069.saturn.lib.mathematics.units.feet
import frc.team4069.saturn.lib.mathematics.units.nativeunits.NativeUnit
import frc.team4069.saturn.lib.mathematics.units.nativeunits.STU
import frc.team4069.saturn.lib.motor.SaturnEncoder
import frc.team4069.saturn.lib.motor.SaturnSRX

object DriveBaseSubsystem : Subsystem() {
    private val leftDrive = SaturnSRX(RobotMap.DRIVEBASE_LEFT_MAIN_SRX, slaveIds = *RobotMap.DRIVEBASE_LEFT_SLAVES_SRX)
    private val leftEncoder = SaturnEncoder(256, 0, 1)

    private val rightDrive =
        SaturnSRX(RobotMap.DRIVEBASE_RIGHT_MAIN_SRX, slaveIds = *RobotMap.DRIVEBASE_RIGHT_SLAVES_SRX)
    private val rightEncoder = SaturnEncoder(256, 8, 9, true)


    val leftPosition: NativeUnit
        get() = leftEncoder.get().STU

    val rightPosition: NativeUnit
        get() = rightEncoder.get().STU

    val leftVelocity: Velocity
        get() = leftEncoder.rate.feet.velocity

    val rightVelocity: Velocity
        get() = leftEncoder.rate.feet.velocity

    private const val stopThreshold = DifferentialDrive.kDefaultQuickStopThreshold
    private const val stopAlpha = DifferentialDrive.kDefaultQuickStopAlpha

//    override var defaultCommand: Command? = OperatorDriveCommand()

    override fun initDefaultCommand() {
        defaultCommand = OperatorDriveCommand()
    }

    private var stopAccumulator = 0.0

    init {
        leftEncoder.distancePerPulse = 0.0075421
        rightEncoder.distancePerPulse = 0.0075421

        leftDrive.apply {
            continuousCurrentLimit = 40
            peakCurrentLimit = 0
            peakCurrentDuration = 0
            currentLimitEnabled = true
        }

        rightDrive.apply {
            continuousCurrentLimit = 40
            peakCurrentLimit = 0
            peakCurrentDuration = 0
            currentLimitEnabled = true
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

    fun reverse() {
        leftEncoder.reversed = !leftEncoder.reversed
        rightEncoder.reversed = !rightEncoder.reversed
    }

    /**
     * Drives using a drive algortihm with constant curvature given [turn], [speed], and [quickTurn]
     *
     * Alternate name is cheesyDrive
     */
    fun curvatureDrive(turn: Double, speed: Double, quickTurn: Boolean) {
        val angularPower: Double
        val overPower: Boolean

        if (quickTurn) {
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

    fun reduceLimits() {
        leftDrive.apply {
            currentLimitEnabled = false
            continuousCurrentLimit = 30
            currentLimitEnabled = true
        }

        rightDrive.apply {
            currentLimitEnabled = false
            continuousCurrentLimit = 30
            currentLimitEnabled = true
        }
    }
}