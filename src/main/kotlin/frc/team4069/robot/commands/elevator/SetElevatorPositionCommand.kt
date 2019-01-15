package frc.team4069.robot.commands.elevator

import edu.wpi.first.wpilibj.command.Command
import frc.team4069.robot.subsystems.ElevatorSubsystem
import frc.team4069.saturn.lib.commands.SaturnCommand
import kotlin.math.abs

class SetElevatorPositionCommand(val pos: ElevatorSubsystem.Position, val instant: Boolean = false) : SaturnCommand(ElevatorSubsystem) {

    init {
        finishCondition += { instant }
        finishCondition += {
            abs(abs(ElevatorSubsystem.position) - abs(pos.ticks)) <= 325
        }
    }

    override suspend fun initialize() {
        ElevatorSubsystem.set(pos)
    }
}