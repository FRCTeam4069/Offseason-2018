package frc.team4069.robot

import edu.wpi.first.wpilibj.GenericHID
import frc.team4069.robot.commands.drive.DriveCommand
import frc.team4069.robot.commands.elevator.SetElevatorPositionCommand
import frc.team4069.robot.subsystems.ElevatorSubsystem
import frc.team4069.robot.subsystems.IntakeSubsystem
import frc.team4069.saturn.lib.hid.*

object OI {

//    private val driveJoystick: Controller
//    val controlJoystick: Controller

    val driveJoystick = xboxController(0) {
        button(kBumperLeft) {
            change(DriveCommand(DriveCommand.Direction.BACKWARDS))
        }

        button(kBumperRight) {
            change(DriveCommand(DriveCommand.Direction.FORWARDS))
        }
    }

    val controlJoystick = xboxController(1) {

        button(kBumperLeft) {
            changeOn {
                IntakeSubsystem.set(-0.5)
            }
            changeOff {
                IntakeSubsystem.set(0.0)
            }
        }

        pov(POVSide.DOWN).changeOn(SetElevatorPositionCommand(ElevatorSubsystem.Position.MINIMUM, instant = true))
        pov(POVSide.RIGHT).changeOn(SetElevatorPositionCommand(ElevatorSubsystem.Position.CARRY, instant = true))
        pov(POVSide.LEFT).changeOn(SetElevatorPositionCommand(ElevatorSubsystem.Position.SWITCH, instant = true))
        pov(POVSide.UP).changeOn(SetElevatorPositionCommand(ElevatorSubsystem.Position.SCALE, instant = true))
    }

    val turningAxis: Double
        get() {
            val axis = driveJoystick.getX(GenericHID.Hand.kLeft)
            return if (axis in 0.0..0.25) {
                0.0
            } else {
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
            val axis = -controlJoystick.getY(GenericHID.Hand.kRight)
            return if (Math.abs(axis) in 0.0..0.2) {
                0.0
            } else {
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
