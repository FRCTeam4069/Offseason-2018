package frc.team4069.robot.subsystems

import edu.wpi.first.wpilibj.drive.DifferentialDrive
import frc.team4069.robot.commands.drive.OperatorDriveCommand
import frc.team4069.saturn.lib.command.Command
import frc.team4069.saturn.lib.command.Subsystem
import frc.team4069.saturn.lib.motor.SaturnEncoder
import frc.team4069.saturn.lib.motor.SaturnSRX

object DriveBaseSubsystem : Subsystem() {
    private val leftDrive = SaturnSRX(12, slaveIds = *intArrayOf(11, 13))
    private val leftEncoder = SaturnEncoder(256, 0, 1)

    private val rightDrive = SaturnSRX(19, reversed = true, slaveIds = *intArrayOf(18, 20))
    private val rightEncoder = SaturnEncoder(256, 8, 9)
    private val drive = DifferentialDrive(leftDrive, rightDrive)

    override var defaultCommand: Command? = OperatorDriveCommand()

    private const val METRES_PER_ROTATION = 0.61

    init {
        leftEncoder.apply {
            setMaxPeriod(0.1)
            setMinRate(10.0)
            distancePerPulse = 5.0
            setReverseDirection(true)
            samplesToAverage = 7
        }

        rightEncoder.apply {
            setMaxPeriod(0.1)
            setMinRate(10.0)
            distancePerPulse = 5.0
            setReverseDirection(true)
            samplesToAverage = 7
        }
    }

    fun stop() {
        drive.stopMotor()
    }

    fun reset() {
        stop()
        leftEncoder.reset()
        rightEncoder.reset()
    }

    fun drive(turn: Double, speed: Double) {
        if(speed == 0.0) {
            drive.curvatureDrive(speed, turn, true)
        }else {
            drive.curvatureDrive(speed, turn, false)
        }
    }

    val distanceTraveledMetres: Double
        get() {
            val leftWheelRotations = Math.abs(leftEncoder.distanceTraveledRotations)
            val rightWheelRotations = Math.abs(rightEncoder.distanceTraveledRotations)

            val average = (leftWheelRotations + rightWheelRotations) / 2

            return average * METRES_PER_ROTATION
        }
}