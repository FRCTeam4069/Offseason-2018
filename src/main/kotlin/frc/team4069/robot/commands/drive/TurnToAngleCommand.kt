package frc.team4069.robot.commands.drive

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.command.Command
import frc.team4069.robot.Robot
import jaci.pathfinder.Pathfinder
import frc.team4069.robot.subsystems.DriveBaseSubsystem as driveBase

class TurnToAngleCommand(val targetAngle: Double, val turnSpeed: Double = 0.7) : Command() {

    val relativeAngle by lazy {
        Pathfinder.boundHalfDegrees(targetAngle)
    }

    init {
        requires(driveBase)
    }

    // How many ticks does the gyroscope angle have to be in range for until the command finishes
    private var counterThreshold = 10
    // Timeout the command after this many milliseconds
    private val timeout = 4000
    // Lower derivativeMultiplier -> derivative has lesser effect on turning speed
    private val derivativeMultiplier = 0.0075
    // True if the robot is turning right
    private var turnRight: Boolean = false
    // Initial gyroscope angle
    private var startAngle: Double = 0.toDouble()
    // Gyroscope angle will be confined to +/- acceptableError degrees from the desired angle
    private val acceptableError = 2.5
    // Counter for tracking how many ticks the gyroscope angle has been in the acceptable range of error
    private var inRangeCounter = 0
    // Current and previous wheel positions, used for calculating derivative
    private var currentGyroscope = 0.0
    private var prevGyroscope = currentGyroscope
    private var startTime: Long = 0
    private var angleAccumulator = 0.0
    // Current and previous times, used for calculating derivative
    private var currentTime: Long = 0
    private var prevTime = currentTime

    /**
     * Calculate relative gyro angle, accounting for jump from 360 to 0
     */
    private fun calculateGyroAngle(): Double {
        val gyroAngle = -Robot.gyro.angle
        return gyroAngle + angleAccumulator
    }

    /**
     * Calculate degrees turned from starting angle
     */
    private fun calculateDelta(): Double {
        val gyroAngle = calculateGyroAngle()
        return gyroAngle - startAngle
    }

    override fun initialize() {
        startAngle = -Robot.gyro.angle
        // If passed angle to turn is positive, turn right
        turnRight = relativeAngle < 0
        currentGyroscope = -Robot.gyro.angle
        prevGyroscope = currentGyroscope
        startTime = System.currentTimeMillis()
        currentTime = startTime
        prevTime = currentTime
    }

    override fun execute() {
        prevGyroscope = currentGyroscope
        currentGyroscope = calculateGyroAngle()
        prevTime = currentTime
        // Detect jump between 0 and 360 and adjust angle accumulator accordingly
        if (currentGyroscope - prevGyroscope > 180) {
            angleAccumulator -= 360.0
        } else if (currentGyroscope - prevGyroscope < -180) {
            angleAccumulator += 360.0
        }
        currentTime = System.currentTimeMillis()
        val delta = calculateDelta()
        val gyroAngle = calculateGyroAngle()
        val degPerSecond = (currentGyroscope - prevGyroscope) / ((currentTime - prevTime).toDouble() / 1000.0)
        // The constant has the effect of narrowing the linearInterpolation to a small range around the desired angle and keeping motor output to a max everywhere else
        val speedConstant = Math.abs(relativeAngle) * (1.0 / 6)

        var motorOutput = lerp(turnSpeed * speedConstant, 0.0, 0.0, relativeAngle,
                gyroAngle - startAngle)

        if (relativeAngle < 0) {
            motorOutput += degPerSecond * derivativeMultiplier
        } else {
            motorOutput -= degPerSecond * derivativeMultiplier
        }

        // Restrict speed to +/- turnSpeedAbsolute
        motorOutput.coerceIn(-turnSpeed..turnSpeed)

        if(turnRight) {
            driveBase.set(ControlMode.PercentOutput, motorOutput, -motorOutput)
        }else {
            driveBase.set(ControlMode.PercentOutput, -motorOutput, motorOutput)
        }

        // If robot is in range of acceptable error, increment the in range counter, otherwise zero it
        if (delta >= relativeAngle - acceptableError && delta <= relativeAngle + acceptableError) {
            inRangeCounter++
        } else {
            inRangeCounter = 0
        }
    }

    fun setCounterThreshold(threshold: Int) {
        counterThreshold = threshold
    }

    override fun isFinished(): Boolean {
        // Turning is complete once robot has been within the acceptable degree of error for counterThreshold ticks
        return inRangeCounter >= counterThreshold || (currentTime - startTime).toInt() >= timeout
    }

//    @Override
//    protected void end() {
//        driveBase.stop();
//    }

    private fun lerp(a: Double, b: Double, a2: Double, b2: Double, c: Double): Double {
        val x = (c - a2) / (b2 - a2)
        return x * b + (1 - x) * a
    }
}