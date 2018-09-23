package frc.team4069.robot.commands.arm

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team4069.robot.subsystems.ArmSubsystem

//class DeployArmCommand : InstantRunnableCommand({ ArmSubsystem.position = ArmSubsystem.MAX_POSITION_TICKS - 300.0}) {
//    init {
//        +ArmSubsystem
//    }
//}

class DeployArmCommand : InstantCommand() {
    init {
        requires(ArmSubsystem)
    }

    override fun initialize() {
        ArmSubsystem.position = ArmSubsystem.MAX_POSITION_TICKS - 200.0
    }
}