package frc.team4069.robot.commands.arm

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team4069.robot.subsystems.ArmSubsystem

class StopArmCommand : InstantCommand() {
    init {
        requires(ArmSubsystem)
    }

    override fun initialize() {
        ArmSubsystem.stop()
        ArmSubsystem.lockPosition()
    }
}