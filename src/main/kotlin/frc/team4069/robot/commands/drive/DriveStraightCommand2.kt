package frc.team4069.robot.commands.drive

import com.ctre.phoenix.motorcontrol.ControlMode
import frc.team4069.robot.subsystems.DriveBaseSubsystem
import frc.team4069.saturn.lib.command.Command
import frc.team4069.saturn.lib.command.condition
import frc.team4069.saturn.lib.pid.PID
import frc.team4069.saturn.lib.pid.PIDConstants

class DriveStraightCommand2(val target: Double) : Command() {
    //TODO: Check and tune
    private val pid = PID(PIDConstants(1.0, 0.0, 0.1, 0.0), target = target)

    init {
        +DriveBaseSubsystem

        finishCondition += condition { pid.atTarget }
    }

    override suspend fun initialize() {
        DriveBaseSubsystem.reset()
    }

    override suspend fun execute() {
        val output = pid.update(DriveBaseSubsystem.distanceTraveledMetres)
        DriveBaseSubsystem.set(ControlMode.PercentOutput, output, output)
    }
}
