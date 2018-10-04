package frc.team4069.robot

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team4069.robot.commands.arm.DeployArmCommand
import frc.team4069.robot.commands.arm.DownArmCommand
import frc.team4069.robot.commands.arm.RetractArmCommand
import frc.team4069.robot.commands.arm.StopArmCommand
import frc.team4069.robot.commands.intake.ToggleOpenIntakeCommand
import frc.team4069.robot.commands.winch.StartWinchCommand
import frc.team4069.robot.commands.winch.StopWinchCommand
import frc.team4069.robot.subsystems.IntakeSubsystem
import frc.team4069.saturn.lib.hid.*

object OI {

//    private val driveJoystick: Controller
//    val controlJoystick: Controller

    val driveJoystick = xboxController(0) {
        button(kA) {
            pressed(DeployArmCommand())
        }

        button(kX) {
            pressed(DownArmCommand())
            released(StopArmCommand())
        }

        button(kB) {
            pressed(RetractArmCommand())
        }
    }
//    val driveJoystick = xboxController(0) {
//        button(kA) {
//            val cmd = DriveCommand(DriveCommand.Direction.FORWARDS)
//            changeOn(cmd)
//            changeOff { cmd.stop() }
//        }
//    }
//
//    val controlJoystick = xboxController(1) {}
    val controlJoystick = xboxController(1) {
        button(kA) {
//            pressed(StartWinchCommand())
//            released(StopWinchCommand())
            pressed(ToggleOpenIntakeCommand())
        }

        button(kB) {
            pressed(StartWinchCommand(reversed = true))
            released(StopWinchCommand())
        }

        button(kY) {
            pressed(object : InstantCommand() {
                init {
                    requires(IntakeSubsystem)
                }

                override fun initialize() {
                    IntakeSubsystem.disableSolenoid()
                }
            })
        }
    }

//    init {
//
//    }

    val turningAxis: Double
        get() {
            val axis = driveJoystick.getX(GenericHID.Hand.kLeft)
            return if(axis in 0.0..0.2) {
                0.0
            }else {
                axis
            }
        }

    val driveSpeed: Double
        get() {
            val forward = driveJoystick.getTriggerAxis(GenericHID.Hand.kRight)
            val backward = driveJoystick.getTriggerAxis(GenericHID.Hand.kLeft)

            return forward - backward
        }

    val elevatorAxis: Double
        get() {
            val axis = controlJoystick.getY(GenericHID.Hand.kRight)
            return if(Math.abs(axis) in 0.0..0.2) {
                0.0
            }else {
                axis
            }
        }

    val intakeAxis: Double
        get() {
            val forward = controlJoystick.getRawAxis(3)
            val backward = controlJoystick.getRawAxis(2)

            return forward - backward
        }
}
