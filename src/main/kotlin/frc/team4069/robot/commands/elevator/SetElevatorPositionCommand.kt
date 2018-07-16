package frc.team4069.robot.commands.elevator

import frc.team4069.saturn.lib.command.Command
import frc.team4069.robot.subsystems.ElevatorSubsystem as elevator

class SetElevatorPositionCommand(private val pos: elevator.Position) : Command() {

    init {
        requires(elevator)
    }

    override fun onCreate() {
        elevator.set(pos)
    }

    override val isFinished: Boolean
        get() = Math.abs(Math.abs(elevator.position) - Math.abs(pos.ticks)) <= 200
}