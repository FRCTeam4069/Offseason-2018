package frc.team4069.robot.commands.arm

import frc.team4069.robot.subsystems.ArmSubsystem
import frc.team4069.saturn.lib.command.builtins.InstantRunnableCommand

class StartArmCommand(val reversed: Boolean = false) : InstantRunnableCommand({ ArmSubsystem.start(reversed) }) {
    init {
        +ArmSubsystem
    }
}