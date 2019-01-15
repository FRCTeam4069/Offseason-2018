package frc.team4069.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.command.Subsystem
import edu.wpi.first.wpilibj.drive.DifferentialDrive
import frc.team4069.robot.RobotMap
import frc.team4069.robot.commands.drive.OperatorDriveCommand
import frc.team4069.saturn.lib.commands.SaturnSubsystem
import frc.team4069.saturn.lib.mathematics.units.amp
import frc.team4069.saturn.lib.mathematics.units.derivedunits.LinearVelocity
import frc.team4069.saturn.lib.mathematics.units.derivedunits.velocity
import frc.team4069.saturn.lib.mathematics.units.feet
import frc.team4069.saturn.lib.mathematics.units.nativeunits.NativeUnit
import frc.team4069.saturn.lib.mathematics.units.nativeunits.STU
import frc.team4069.saturn.lib.mathematics.units.second
import frc.team4069.saturn.lib.motor.NativeSaturnSRX
import frc.team4069.saturn.lib.motor.SaturnEncoder
import frc.team4069.saturn.lib.motor.SaturnSRX

object DriveBaseSubsystem : SaturnSubsystem() {
    private val leftMaster = NativeSaturnSRX(RobotMap.DRIVEBASE_LEFT_MAIN_SRX)
    private val leftSlave1 = NativeSaturnSRX(RobotMap.DRIVEBASE_LEFT_SLAVES_SRX[0])
    private val leftSlave2 = NativeSaturnSRX(RobotMap.DRIVEBASE_LEFT_SLAVES_SRX[1])

    private val rightMaster = NativeSaturnSRX(RobotMap.DRIVEBASE_RIGHT_MAIN_SRX)
    private val rightSlave1 = NativeSaturnSRX(RobotMap.DRIVEBASE_RIGHT_SLAVES_SRX[0])
    private val rightSlave2 = NativeSaturnSRX(RobotMap.DRIVEBASE_RIGHT_SLAVES_SRX[1])


    private const val stopThreshold = DifferentialDrive.kDefaultQuickStopThreshold
    private const val stopAlpha = DifferentialDrive.kDefaultQuickStopAlpha

    override fun teleopReset() {
        OperatorDriveCommand().start()
    }

    private var stopAccumulator = 0.0

    init {
        leftMaster.apply {
            continuousCurrentLimit = 40.amp
            peakCurrentLimit = 0.amp
            peakCurrentLimitDuration = 0.second
            currentLimitingEnabled = true
        }

        rightMaster.apply {
            continuousCurrentLimit = 40.amp
            peakCurrentLimit = 0.amp
            peakCurrentLimitDuration = 0.second
            currentLimitingEnabled = true
        }
        leftSlave1.follow(leftMaster)
        leftSlave2.follow(leftMaster)

        rightSlave1.follow(rightMaster)
        rightSlave2.follow(rightMaster)
    }

    fun stop() {
        leftMaster.stopMotor()
        rightMaster.stopMotor()
    }

    fun reset() {
        stop()
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
        leftMaster.set(mode, left)
        rightMaster.set(mode, right)
    }

    fun reduceLimits() {
        leftMaster.apply {
            currentLimitingEnabled = false
            continuousCurrentLimit = 30.amp
            currentLimitingEnabled = true
        }

        rightMaster.apply {
            currentLimitingEnabled = false
            continuousCurrentLimit = 30.amp
            currentLimitingEnabled = true
        }
    }
}
