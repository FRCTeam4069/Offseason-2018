package frc.team4069.robot.commands.arm

import frc.team4069.saturn.lib.command.InstantCommand
import frc.team4069.robot.subsystems.ArmSubsystem as arm

class StartArmCommand(private val reversed: Boolean = false) : InstantCommand() {
    init {
        requires(arm)
    }

    override fun onCreate() {
        arm.start(reversed)
    }
}