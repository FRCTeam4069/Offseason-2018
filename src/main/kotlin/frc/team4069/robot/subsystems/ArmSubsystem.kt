package frc.team4069.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import frc.team4069.saturn.lib.command.Subsystem
import frc.team4069.saturn.lib.motor.SaturnSRX

object ArmSubsystem : Subsystem() {
    private val talon = SaturnSRX(24)

    init {
        talon.apply {
            f = 1.0
            p = 0.5

            motionCruiseVelocity = 800
            motionAcceleration = 400
        }
    }

    fun start(reversed: Boolean) {
        talon.set(ControlMode.PercentOutput, if(reversed) -0.1 else 0.2)
    }

    fun stop() = talon.stop()

    var position: Double
        get() = talon.getSelectedSensorPosition(0).toDouble()
        set(value) = talon.set(ControlMode.MotionMagic, value)

    fun reset() {
        stop()
        talon.setSelectedSensorPosition(0, 0, 0)
    }

    const val MAX_POSITION_TICKS = 2700
}