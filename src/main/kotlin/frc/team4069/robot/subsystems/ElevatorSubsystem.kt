package frc.team4069.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.command.Scheduler
import edu.wpi.first.wpilibj.command.Subsystem
import frc.team4069.robot.OI
import frc.team4069.robot.RobotMap
import frc.team4069.robot.commands.elevator.OperatorControlElevatorCommand
import frc.team4069.robot.commands.elevator.SetElevatorPositionCommand
import frc.team4069.saturn.lib.motor.SaturnSRX

object ElevatorSubsystem : Subsystem() {
//    override var defaultCommand: Command? = OperatorControlElevatorCommand()

    override fun initDefaultCommand() {
        defaultCommand = OperatorControlElevatorCommand()
    }

    private val talon = SaturnSRX(RobotMap.ELEVATOR_MAIN_SRX, reversed = true, slaveIds = *intArrayOf(RobotMap.ELEVATOR_SLAVE_SRX))

    private const val MAX_POSITION_TICKS = -29000

    init {
        talon.apply {
            invertSensorPhase = true

            p = 0.5
            d = 0.1
            f = 0.5

            motionAcceleration = 2500
            motionCruiseVelocity = 3000

            configPeakCurrentLimit(35, 0)
            configPeakCurrentDuration(250, 0)
            configContinuousCurrentLimit(32, 0)
            enableCurrentLimit(true)

            configReverseSoftLimitThreshold(MAX_POSITION_TICKS, 0)
            configReverseSoftLimitEnable(true, 0)
        }
    }

    override fun periodic() {
        val angle = OI.controlJoystick.pov
        val scheduler = Scheduler.getInstance()
        when (angle) {
            90 -> scheduler.add(SetElevatorPositionCommand(Position.SWITCH))
            0 -> scheduler.add(SetElevatorPositionCommand(Position.SCALE))
            180 -> scheduler.add(SetElevatorPositionCommand(Position.MINIMUM))
            270 -> scheduler.add(SetElevatorPositionCommand(Position.CARRY))
        }
    }

    fun set(mode: ControlMode, value: Double) = talon.set(mode, value)

    fun set(pos: Position) = set(ControlMode.MotionMagic, pos.ticks.toDouble())

    val position: Int
        get() = talon.getSelectedSensorPosition(0)

    enum class Position(val ticks: Int) {
        MINIMUM(0),
        CARRY(-2500),
        SWITCH(-8000),
        SCALE(MAX_POSITION_TICKS + 100)
    }
}
