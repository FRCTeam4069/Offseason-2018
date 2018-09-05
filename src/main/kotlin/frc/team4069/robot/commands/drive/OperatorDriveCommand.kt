package frc.team4069.robot.commands.drive

import edu.wpi.first.wpilibj.command.Command
import frc.team4069.robot.OI
import frc.team4069.robot.subsystems.DriveBaseSubsystem

class OperatorDriveCommand : Command() {

    init {
        requires(DriveBaseSubsystem)
    }

    override fun initialize() {
        DriveBaseSubsystem.stop()
    }

    override fun execute() {
        val turning = OI.turningAxis
        val speed = OI.driveSpeed

        DriveBaseSubsystem.curvatureDrive(turning, speed, speed == 0.0)
    }

    override fun isFinished(): Boolean {
        return false
    }
}