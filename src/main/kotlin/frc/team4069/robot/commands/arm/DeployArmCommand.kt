package frc.team4069.robot.commands.arm

import frc.team4069.saturn.lib.command.InstantCommand
import frc.team4069.robot.subsystems.ArmSubsystem as arm

class DeployArmCommand : InstantCommand() {

    init {
        requires(arm)
    }

    override fun onCreate() {
        arm.position = arm.MAX_POSITION_TICKS - 300.0
    }
}