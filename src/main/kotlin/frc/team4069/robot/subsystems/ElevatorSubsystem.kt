package frc.team4069.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.command.Subsystem
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team4069.robot.RobotMap
import frc.team4069.robot.commands.elevator.OperatorControlElevatorCommand
import frc.team4069.saturn.lib.commands.SaturnSubsystem
import frc.team4069.saturn.lib.mathematics.units.amp
import frc.team4069.saturn.lib.mathematics.units.millisecond
import frc.team4069.saturn.lib.mathematics.units.nativeunits.STUPer100ms
import frc.team4069.saturn.lib.mathematics.units.nativeunits.STUPer100msPerSecond
import frc.team4069.saturn.lib.mathematics.units.second
import frc.team4069.saturn.lib.motor.NativeSaturnSRX
import frc.team4069.saturn.lib.motor.SaturnSRX

object ElevatorSubsystem : SaturnSubsystem() {

    override fun teleopReset() {
        OperatorControlElevatorCommand().start()
    }

    private val talon =
        NativeSaturnSRX(
            RobotMap.ELEVATOR_MAIN_SRX
//            filter = LowPassFilter(150)
        )
    private val follower = NativeSaturnSRX(RobotMap.ELEVATOR_SLAVE_SRX)

    private const val MAX_POSITION_TICKS = 29000

    init {
        follower.inverted = true
        follower.follow(talon)
        talon.apply {
            this.setSensorPhase(true)

            kP = 0.7
            kD = 0.01
            kF = 0.5

            motionAcceleration = 2500.STUPer100msPerSecond
            motionCruiseVelocity = 3000.STUPer100ms

            peakCurrentLimit = 35.amp
            peakCurrentLimitDuration = 250.millisecond
            continuousCurrentLimit = 32.amp
            currentLimitingEnabled = true
//            forwardSoftLimitThreshold = MAX_POSITION_TICKS
//            forwardSoftLimitEnabled = true
        }
    }

    fun set(mode: ControlMode, value: Double) = talon.set(mode, value)

    fun set(pos: Position) = set(ControlMode.MotionMagic, pos.ticks.toDouble())

    val position: Int
        get() = talon.getSelectedSensorPosition(0)

    fun reduceLimits() {
        talon.apply {
            currentLimitingEnabled = false
            peakCurrentLimit = 0.amp
            peakCurrentLimitDuration = 0.second

            continuousCurrentLimit = 26.amp
            currentLimitingEnabled = true
        }
    }

    enum class Position(val ticks: Int) {
        MINIMUM(0),
        CARRY(2500),
        SWITCH(8000),
        SCALE(MAX_POSITION_TICKS - 100)
    }
}
