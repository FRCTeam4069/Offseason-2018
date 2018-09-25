package frc.team4069.robot.commands.elevator

import edu.wpi.first.wpilibj.command.Command
import frc.team4069.robot.subsystems.ElevatorSubsystem
import kotlin.math.abs

class SetElevatorPositionCommand(val pos: ElevatorSubsystem.Position) : Command() {

    init {
        requires(ElevatorSubsystem)
    }

    override fun initialize() {
        ElevatorSubsystem.set(pos)
    }

    override fun isFinished(): Boolean {
        return abs(abs(ElevatorSubsystem.position) - abs(pos.ticks)) <= 200
    }
}