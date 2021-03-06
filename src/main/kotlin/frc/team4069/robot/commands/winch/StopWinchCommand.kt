package frc.team4069.robot.commands.winch

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team4069.robot.subsystems.WinchSubsystem

class StopWinchCommand : InstantCommand() {
    init {
        requires(WinchSubsystem)
    }

    override fun initialize() {
        WinchSubsystem.stop()
    }
}