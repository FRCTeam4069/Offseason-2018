package frc.team4069.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.command.Subsystem
import frc.team4069.robot.RobotMap
import frc.team4069.saturn.lib.motor.SaturnSRX

object WinchSubsystem : Subsystem() {
    val talon = SaturnSRX(RobotMap.WINCH_MAIN_SRX, slaveIds = *intArrayOf(RobotMap.WINCH_SLAVE_SRX))

    fun start(reversed: Boolean) {
        talon.set(ControlMode.PercentOutput, if(reversed) -0.5 else 1.0)
    }

    fun stop() {
        talon.stop()
    }

    override fun initDefaultCommand() {}
}