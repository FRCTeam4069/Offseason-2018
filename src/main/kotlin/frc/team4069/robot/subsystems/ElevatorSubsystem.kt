package frc.team4069.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.command.Subsystem
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team4069.robot.RobotMap
import frc.team4069.robot.commands.elevator.OperatorControlElevatorCommand
import frc.team4069.saturn.lib.motor.SaturnSRX

object ElevatorSubsystem : Subsystem() {

    override fun initDefaultCommand() {
        defaultCommand = OperatorControlElevatorCommand()
    }

    private val talon =
        SaturnSRX(
            RobotMap.ELEVATOR_MAIN_SRX
//            filter = LowPassFilter(150)
        )
    private val follower = SaturnSRX(RobotMap.ELEVATOR_SLAVE_SRX)

    private const val MAX_POSITION_TICKS = 29000

    init {
        follower.inverted = true
        follower.follow(talon)
        talon.apply {
            invertSensorPhase = true

            p = 0.7
            d = 0.01
            f = 0.5

            motionAcceleration = 2500
            motionCruiseVelocity = 3000

            peakCurrentLimit = 35
            peakCurrentDuration = 250
            continuousCurrentLimit = 32
            currentLimitEnabled = true
//            forwardSoftLimitThreshold = MAX_POSITION_TICKS
//            forwardSoftLimitEnabled = true
        }
    }

    fun set(mode: ControlMode, value: Double) = talon.set(mode, value)

    fun set(pos: Position) = set(ControlMode.MotionMagic, pos.ticks.toDouble())

    override fun periodic() {
        SmartDashboard.putNumber("Elevator current draw", talon.outputCurrent)
    }

    val position: Int
        get() = talon.getSelectedSensorPosition(0)

    fun reduceLimits() {
        talon.apply {
            currentLimitEnabled = false
            peakCurrentLimit = 0
            peakCurrentDuration = 0

            continuousCurrentLimit = 26
            currentLimitEnabled = true
        }
    }

    enum class Position(val ticks: Int) {
        MINIMUM(0),
        CARRY(2500),
        SWITCH(8000),
        SCALE(MAX_POSITION_TICKS - 100)
    }
}
