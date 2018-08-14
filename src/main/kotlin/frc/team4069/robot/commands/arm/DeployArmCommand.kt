package frc.team4069.robot.commands.arm

import frc.team4069.robot.subsystems.ArmSubsystem
import frc.team4069.saturn.lib.command.builtins.InstantRunnableCommand

class DeployArmCommand : InstantRunnableCommand({ ArmSubsystem.position = ArmSubsystem.MAX_POSITION_TICKS - 300.0}) {
    init {
        +ArmSubsystem
    }
}