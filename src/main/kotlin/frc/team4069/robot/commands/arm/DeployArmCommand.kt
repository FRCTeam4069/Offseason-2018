package frc.team4069.robot.commands.arm

import frc.team4069.robot.subsystems.ArmSubsystem
import frc.team4069.saturn.lib.command.Command

class DeployArmCommand : Command() {

    init {
        requires(ArmSubsystem)
    }

    override fun onCreate() {
        ArmSubsystem.position = ArmSubsystem.MAX_POSITION_TICKS - 300.0
    }

    override val isFinished = true
}