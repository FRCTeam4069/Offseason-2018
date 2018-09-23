package frc.team4069.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.command.Subsystem
import frc.team4069.robot.RobotMap
import frc.team4069.saturn.lib.motor.SaturnSRX

object ArmSubsystem : Subsystem() {
    private val talon = SaturnSRX(RobotMap.ARM_SRX)

    init {
        talon.apply {
            f = 1.0
            p = 0.5

            motionCruiseVelocity = 800
            motionAcceleration = 400

            configForwardSoftLimitThreshold(2700, 0)
            configForwardSoftLimitEnable(true, 0)

            configReverseSoftLimitThreshold(0, 0)
            configReverseSoftLimitEnable(true, 0)
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

    override fun initDefaultCommand() {
    }

    fun lockPosition() {
        talon.set(ControlMode.MotionMagic, position)
    }

    const val MAX_POSITION_TICKS = 2700
}