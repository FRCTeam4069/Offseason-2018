package frc.team4069.robot.commands.drive

import frc.team4069.saturn.lib.command.Command
import frc.team4069.saturn.lib.pid.PID
import frc.team4069.saturn.lib.pid.PIDConstants
import frc.team4069.robot.subsystems.DriveBaseSubsystem as driveBase

class DriveStraightCommand2(private val target: Double) : Command() {
    //TODO: Check and tune
    private val pid = PID(PIDConstants(1.0, 0.0, 0.1, 0.0), target = target)
    private var lastDistance = Double.NaN

    init {
        requires(driveBase)
    }

    override fun onCreate() {
        driveBase.reset()
    }

    override fun onSuspend() {
        lastDistance = driveBase.distanceTraveledMetres
    }

    override fun onResume() {
        driveBase.reset()
        pid.target = target - lastDistance
    }

    override fun periodic() {
        driveBase.drive(0.0, pid.update(driveBase.distanceTraveledMetres))
    }

    override val isFinished: Boolean
        get() = pid.atTarget
}