package frc.team4069.robot.commands.drive

import frc.team4069.robot.OI
import frc.team4069.robot.subsystems.DriveBaseSubsystem
import frc.team4069.saturn.lib.command.Command

class OperatorDriveCommand : Command() {

    init {
        requires(DriveBaseSubsystem)
    }

    override fun onCreate() {
        DriveBaseSubsystem.stop()
    }

    override fun periodic() {

        val turning = OI.steeringAxis
        val speed = OI.driveSpeed

        DriveBaseSubsystem.drive(turning, speed)
    }

    override val isFinished: Boolean = false
}