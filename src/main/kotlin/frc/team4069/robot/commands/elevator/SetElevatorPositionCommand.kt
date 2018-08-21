package frc.team4069.robot.commands.elevator

import frc.team4069.robot.subsystems.ElevatorSubsystem
import frc.team4069.saturn.lib.command.Command
import frc.team4069.saturn.lib.command.condition
import kotlin.math.abs

class SetElevatorPositionCommand(val pos: ElevatorSubsystem.Position) : Command() {

    init {
        +ElevatorSubsystem

        finishCondition += condition {
            abs(abs(ElevatorSubsystem.position) - abs(pos.ticks)) <= 200
        }
    }

    override suspend fun initialize() {
        println("Command initialized")
        ElevatorSubsystem.set(pos)
    }
}