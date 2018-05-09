package frc.team4069.robot.commands.arm

import frc.team4069.robot.subsystems.ArmSubsystem
import frc.team4069.saturn.lib.command.Command

class StartArmCommand(val reversed: Boolean = false) : Command() {
    init {
        requires(ArmSubsystem)
    }

    override fun onCreate() {
        ArmSubsystem.start(reversed)
    }

    override val isFinished = true
}