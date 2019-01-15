package frc.team4069.robot.commands.drive

import edu.wpi.first.wpilibj.command.Command
import frc.team4069.robot.OI
import frc.team4069.robot.subsystems.DriveBaseSubsystem
import frc.team4069.saturn.lib.commands.SaturnCommand

class OperatorDriveCommand : SaturnCommand(DriveBaseSubsystem) {

    override suspend fun initialize() {
        DriveBaseSubsystem.stop()
    }

    override suspend fun execute() {
        val turning = OI.turningAxis
        val speed = OI.driveSpeed

        DriveBaseSubsystem.curvatureDrive(turning, speed, speed == 0.0)
    }
}