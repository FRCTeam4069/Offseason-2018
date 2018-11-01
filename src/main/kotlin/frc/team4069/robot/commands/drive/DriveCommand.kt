package frc.team4069.robot.commands.drive

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.command.Command
import frc.team4069.robot.subsystems.DriveBaseSubsystem

class DriveCommand(val dir: Direction) : Command() {

    override fun isFinished() = false

    init {
//        +DriveBaseSubsystem
        requires(DriveBaseSubsystem)
    }

    override fun initialize() {
        println("Initializing command")
        if (dir == Direction.FORWARDS) {
            DriveBaseSubsystem.set(ControlMode.PercentOutput, 0.5, 0.5)
        } else {
            DriveBaseSubsystem.set(ControlMode.PercentOutput, -0.5, -0.5)
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