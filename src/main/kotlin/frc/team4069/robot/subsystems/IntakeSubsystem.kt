package frc.team4069.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import frc.team4069.robot.commands.intake.OperatorControlIntakeCommand
import frc.team4069.saturn.lib.command.Subsystem
import frc.team4069.saturn.lib.motor.SaturnSRX

object IntakeSubsystem : Subsystem() {
    override val defaultCommand = OperatorControlIntakeCommand()
    private val talon = SaturnSRX(14, slaveIds = *intArrayOf(21))

    fun set(spd: Double) = talon.set(ControlMode.PercentOutput, spd)

    fun stop() = talon.stop()
}