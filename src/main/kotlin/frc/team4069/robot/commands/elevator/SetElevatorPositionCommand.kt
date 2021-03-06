package frc.team4069.robot.commands.elevator

import edu.wpi.first.wpilibj.command.Command
import frc.team4069.robot.subsystems.ElevatorSubsystem
import kotlin.math.abs

class SetElevatorPositionCommand(val pos: ElevatorSubsystem.Position, val instant: Boolean = false) : Command() {

    init {
        requires(ElevatorSubsystem)
    }

    override fun initialize() {
        ElevatorSubsystem.set(pos)
    }

    override fun isFinished() = instant || abs(abs(ElevatorSubsystem.position) - abs(pos.ticks)) <= 325
}