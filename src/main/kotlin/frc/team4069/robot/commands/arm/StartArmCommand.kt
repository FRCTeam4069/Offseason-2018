package frc.team4069.robot.commands.arm

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team4069.robot.subsystems.ArmSubsystem

//class StartArmCommand(val reversed: Boolean = false) : InstantRunnableCommand({ ArmSubsystem.start(reversed) }) {
//    init {
//        +ArmSubsystem
//    }
//}

class StartArmCommand(val reversed: Boolean = false) : InstantCommand() {
    init {
        requires(ArmSubsystem)
    }

    override fun initialize() {
        ArmSubsystem.start(reversed)
    }
}