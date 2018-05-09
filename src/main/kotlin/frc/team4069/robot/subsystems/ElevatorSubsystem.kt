package frc.team4069.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import frc.team4069.robot.commands.elevator.OperatorControlElevatorCommand
import frc.team4069.saturn.lib.command.Subsystem
import frc.team4069.saturn.lib.motor.SaturnSRX

object ElevatorSubsystem : Subsystem() {
    override val defaultCommand = OperatorControlElevatorCommand()
    private val talon = SaturnSRX(16, reversed = true, slaveIds = *intArrayOf(15))

    private const val MAX_POSITION_TICKS = -29000

    init {
        talon.apply {
            invertSensorPhase = false

            p = 0.6
            d = 0.1
            f = 0.5

            motionAcceleration = 2500
            motionCruiseVelocity = 3000

            configReverseSoftLimitThreshold(MAX_POSITION_TICKS, 0)
            configReverseSoftLimitEnable(true, 0)
        }
    }

    fun set(mode: ControlMode, value: Double) = talon.set(mode, value)

    fun set(pos: Position) = set(ControlMode.MotionMagic, pos.ticks.toDouble())

    val position: Int
        get() = talon.getSelectedSensorPosition(0)

    enum class Position(val ticks: Int) {
        HALFWAY(-3000)
    }
}
