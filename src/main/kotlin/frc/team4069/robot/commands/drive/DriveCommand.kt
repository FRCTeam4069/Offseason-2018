package frc.team4069.robot.commands.drive

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team4069.robot.subsystems.DriveBaseSubsystem

class DriveCommand(val dir: Direction) : InstantCommand() {

    init {
//        +DriveBaseSubsystem
        requires(DriveBaseSubsystem)
    }

    override fun initialize() {
        println("Initializing command")
        if (dir == Direction.FORWARDS) {
            DriveBaseSubsystem.set(ControlMode.PercentOutput, 0.25, 0.25)
        } else {
            DriveBaseSubsystem.set(ControlMode.PercentOutput, -0.7, -0.7)
        }
    }

    override fun end() {
        DriveBaseSubsystem.stop()
    }

    enum class Direction {
        FORWARDS,
        BACKWARDS
    }
}