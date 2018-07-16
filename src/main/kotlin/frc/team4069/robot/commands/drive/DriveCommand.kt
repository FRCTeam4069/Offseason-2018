package frc.team4069.robot.commands.drive

import frc.team4069.robot.subsystems.DriveBaseSubsystem
import frc.team4069.saturn.lib.command.Command

class DriveCommand(val dir: Direction) : Command() {

    init {
        requires(DriveBaseSubsystem)
    }

    override fun onCreate() {
        if(dir == Direction.FORWARDS) {
            DriveBaseSubsystem.drive(0.0, 0.7)
        }else {
            DriveBaseSubsystem.drive(0.0, -0.7)
        }
    }

    override val isFinished = true

    enum class Direction {
        FORWARDS,
        BACKWARDS
    }
}