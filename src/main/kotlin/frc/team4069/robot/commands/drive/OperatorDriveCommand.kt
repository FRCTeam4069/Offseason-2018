package frc.team4069.robot.commands.drive

import frc.team4069.robot.OI
import frc.team4069.robot.subsystems.DriveBaseSubsystem
import frc.team4069.saturn.lib.command.Command

class OperatorDriveCommand : Command() {

    init {
        +DriveBaseSubsystem
    }

    override suspend fun initialize() {
        DriveBaseSubsystem.stop()
    }

    override suspend fun execute() {
        val turning = OI.turningAxis
        val speed = OI.driveSpeed

        DriveBaseSubsystem.curvatureDrive(turning, speed, speed == 0.0)
    }
}