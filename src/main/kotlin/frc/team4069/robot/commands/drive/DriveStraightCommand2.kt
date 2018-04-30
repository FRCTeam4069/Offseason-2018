package frc.team4069.robot.commands.drive

import edu.wpi.first.wpilibj.command.Command
import frc.team4069.robot.subsystems.DriveBaseSubsystem
import frc.team4069.saturn.lib.pid.PID
import frc.team4069.saturn.lib.pid.PIDConstants

class DriveStraightCommand2(target: Double) : Command() {
    //TODO: Check and tune
    private val pid = PID(PIDConstants(1.0, 0.0, 0.1, 0.0), target = target)

    init {
        requires(DriveBaseSubsystem)
    }

    override fun initialize() {
        DriveBaseSubsystem.reset()
    }

    override fun execute() {
        DriveBaseSubsystem.drive(0.0, pid.update(DriveBaseSubsystem.distanceTraveledMetres))
    }

    override fun isFinished(): Boolean {
        return pid.atTarget
    }
}