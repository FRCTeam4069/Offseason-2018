package frc.team4069.robot.auto

import edu.wpi.first.wpilibj.command.CommandGroup

abstract class AutoMode {
    abstract fun build(): CommandGroup
}