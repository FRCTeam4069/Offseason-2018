package frc.team4069.robot.commands.drive

import com.ctre.phoenix.motorcontrol.ControlMode
import frc.team4069.robot.subsystems.DriveBaseSubsystem
import frc.team4069.saturn.lib.command.Command

class DriveCommand(val dir: Direction) : Command() {

    init {
        +DriveBaseSubsystem
    }

    override suspend fun initialize() {
        if(dir == Direction.FORWARDS) {
            DriveBaseSubsystem.set(ControlMode.PercentOutput, 0.25, 0.25)
        }else {
            DriveBaseSubsystem.set(ControlMode.PercentOutput, -0.7, -0.7)
        }
    }

    override suspend fun dispose() {
        DriveBaseSubsystem.stop()
    }

    enum class Direction {
        FORWARDS,
        BACKWARDS
    }
}