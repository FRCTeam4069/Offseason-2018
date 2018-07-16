package frc.team4069.robot.commands.drive

import frc.team4069.robot.subsystems.DriveBaseSubsystem
import frc.team4069.saturn.lib.command.Command
import frc.team4069.saturn.lib.pid.PID
import frc.team4069.saturn.lib.pid.PIDConstants

class DriveStraightCommand2(val target: Double) : Command() {
    //TODO: Check and tune
    private val pid = PID(PIDConstants(1.0, 0.0, 0.1, 0.0), target = target)
    private var lastDistance = Double.NaN

    init {
        requires(DriveBaseSubsystem)
    }

    override fun onCreate() {
        DriveBaseSubsystem.reset()
    }

    override fun onSuspend() {
        lastDistance = DriveBaseSubsystem.distanceTraveledMetres
    }

    override fun onResume() {
        DriveBaseSubsystem.reset()
        pid.target = target - lastDistance
    }

    override fun periodic() {
        DriveBaseSubsystem.drive(0.0, pid.update(DriveBaseSubsystem.distanceTraveledMetres))
    }

    override val isFinished: Boolean
        get() = pid.atTarget
}