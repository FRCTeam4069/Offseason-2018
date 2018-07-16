package frc.team4069.robot.commands.drive

import frc.team4069.robot.subsystems.DriveBaseSubsystem as driveBase
import frc.team4069.saturn.lib.command.InstantCommand

class DriveCommand(private val dir: Direction) : InstantCommand() {

    init {
        requires(driveBase)
    }

    override fun onCreate() {
        if(dir == Direction.FORWARDS) {
            driveBase.drive(0.0, 0.7)
        }else {
            driveBase.drive(0.0, -0.7)
        }
    }

    enum class Direction {
        FORWARDS,
        BACKWARDS
    }
}