package frc.team4069.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj.command.Subsystem
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team4069.robot.RobotMap
import frc.team4069.robot.commands.intake.OperatorControlIntakeCommand
import frc.team4069.saturn.lib.commands.SaturnSubsystem
import frc.team4069.saturn.lib.motor.NativeSaturnSRX
import frc.team4069.saturn.lib.motor.SaturnSRX

object IntakeSubsystem : SaturnSubsystem() {
    private val leftSide = NativeSaturnSRX(RobotMap.INTAKE_LEFT_SRX)
    private val rightSide = NativeSaturnSRX(RobotMap.INTAKE_RIGHT_SRX)

    init {
        rightSide.follow(leftSide)
    }

    override fun teleopReset() {
        OperatorControlIntakeCommand().start()
    }

    fun set(spd: Double) {
        leftSide.set(ControlMode.PercentOutput, spd)
    }

    val outputCurrent: Double
        get() = leftSide.outputCurrent

    fun stop() = leftSide.stopMotor()

}