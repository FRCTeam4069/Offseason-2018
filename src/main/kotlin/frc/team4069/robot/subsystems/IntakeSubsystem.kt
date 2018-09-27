package frc.team4069.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj.command.Subsystem
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team4069.robot.RobotMap
import frc.team4069.robot.commands.intake.OperatorControlIntakeCommand
import frc.team4069.saturn.lib.motor.SaturnSRX

object IntakeSubsystem : Subsystem() {
    private val leftSide = SaturnSRX(RobotMap.INTAKE_LEFT_SRX)
    private val rightSide = SaturnSRX(RobotMap.INTAKE_RIGHT_SRX)

    private val outputSolenoid = DoubleSolenoid(0, 7)

    override fun initDefaultCommand() {
        defaultCommand = OperatorControlIntakeCommand()
    }

    fun set(spd: Double, reverseRight: Boolean) {
        rightSide.set(ControlMode.PercentOutput, spd)
        leftSide.set(ControlMode.PercentOutput, if(reverseRight) spd * -0.25 else spd)
    }

    override fun periodic() {
        SmartDashboard.putNumber("Intake current", leftSide.outputCurrent)
    }

    fun toggleSolenoid() {
        when(outputSolenoid.get()) {
            DoubleSolenoid.Value.kForward -> outputSolenoid.set(DoubleSolenoid.Value.kReverse)
            else -> outputSolenoid.set(DoubleSolenoid.Value.kForward)
        }
        println("New out value is ${outputSolenoid.get()}")
    }

    val outputCurrent: Double
        get() = leftSide.outputCurrent

    fun stop() = leftSide.stop()
}