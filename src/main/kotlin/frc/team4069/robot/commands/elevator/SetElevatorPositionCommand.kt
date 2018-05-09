package frc.team4069.robot.commands.elevator

import frc.team4069.robot.subsystems.ElevatorSubsystem
import frc.team4069.saturn.lib.command.Command

class SetElevatorPositionCommand(val pos: ElevatorSubsystem.Position) : Command() {

    init {
        requires(ElevatorSubsystem)
    }

    override fun onCreate() {
        ElevatorSubsystem.set(pos)
    }

    override val isFinished: Boolean
        get() = Math.abs(Math.abs(ElevatorSubsystem.position) - Math.abs(pos.ticks)) <= 200
}