package frc.team4069.robot.commands.drive

import frc.team4069.robot.OI
import frc.team4069.saturn.lib.command.Command
import frc.team4069.robot.subsystems.DriveBaseSubsystem as driveBase

class OperatorDriveCommand : Command() {

    init {
        requires(driveBase)
    }

    override fun onCreate() {
        driveBase.stop()
    }

    override fun periodic() {

        val turning = OI.turningAxis
        val speed = OI.driveSpeed

        driveBase.drive(turning, speed)
    }

    override val isFinished: Boolean = false
}