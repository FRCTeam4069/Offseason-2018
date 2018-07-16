package frc.team4069.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import frc.team4069.robot.OI
import frc.team4069.robot.commands.intake.OperatorControlIntakeCommand
import frc.team4069.saturn.lib.command.Subsystem
import frc.team4069.saturn.lib.motor.SaturnSRX
import frc.team4069.saturn.lib.pneumatics.Solenoid

object IntakeSubsystem : Subsystem() {
    override val defaultCommand = OperatorControlIntakeCommand()
    //    private val talon = SaturnSRX(14, slaveIds = *intArrayOf(21))
    private val leftSideMotor = SaturnSRX(14)
    private val rightSideMotor = SaturnSRX(21)
    private val openingSolenoid = Solenoid(port = 1) // TODO: Set port properly

//    fun set(spd: Double) = talon.set(ControlMode.PercentOutput, spd)

    /**
     * Represents whether the intake is currently forced open, or closed (closed being cylinder in, solenoid set to false)
     */
    val isClosed: Boolean
        get() = !openingSolenoid.output

    /**
     * Releases the clamp on the cube (extends the cylinder)
     */
    fun release() {
        openingSolenoid.output = true
    }

    /**
     * Clamps on the cube (retracts the cylinder/closes the intake)
     */
    fun grab() {
        openingSolenoid.output = false
    }

    fun set(spd: Double) {
        leftSideMotor.set(ControlMode.PercentOutput, if(OI.intakeLeftReversed) {
            -0.5 * spd
        }else {
            spd
        })
        rightSideMotor.set(ControlMode.PercentOutput, spd)
    }

    fun stop() {
        leftSideMotor.stop()
        rightSideMotor.stop()
    }
}