package frc.team4069.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.command.Subsystem
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team4069.robot.commands.intake.OperatorControlIntakeCommand
import frc.team4069.saturn.lib.motor.SaturnSRX

object IntakeSubsystem : Subsystem() {
//    override var defaultCommand: Command? = OperatorControlIntakeCommand()
    private val leftSide = SaturnSRX(14)
    private val rightSide = SaturnSRX(21)

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

    val outputCurrent: Double
        get() = leftSide.outputCurrent

    fun stop() = leftSide.stop()
}