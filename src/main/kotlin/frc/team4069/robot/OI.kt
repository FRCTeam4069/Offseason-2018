package frc.team4069.robot

import edu.wpi.first.wpilibj.GenericHID
import frc.team4069.robot.commands.arm.DeployArmCommand
import frc.team4069.robot.commands.arm.DownArmCommand
import frc.team4069.robot.commands.arm.RetractArmCommand
import frc.team4069.robot.commands.arm.StopArmCommand
import frc.team4069.robot.commands.drive.DriveCommand
import frc.team4069.robot.commands.elevator.SetElevatorPositionCommand
import frc.team4069.robot.commands.intake.ToggleOpenIntakeCommand
import frc.team4069.robot.commands.winch.StartWinchCommand
import frc.team4069.robot.commands.winch.StopWinchCommand
import frc.team4069.robot.subsystems.ElevatorSubsystem
import frc.team4069.saturn.lib.hid.*

object OI {

//    private val driveJoystick: Controller
//    val controlJoystick: Controller

    val driveJoystick = xboxController(0) {
        button(kA) {
            changeOn(DeployArmCommand())
        }

        button(kX) {
            changeOn(DownArmCommand())
            changeOff(StopArmCommand())
        }

        button(kB) {
            changeOn(RetractArmCommand())
        }

        button(kBumperLeft) {
            change(DriveCommand(DriveCommand.Direction.BACKWARDS))
        }

        button(kBumperRight) {
            change(DriveCommand(DriveCommand.Direction.FORWARDS))
        }
    }

    val controlJoystick = xboxController(1) {
        button(kA) {
            changeOn(ToggleOpenIntakeCommand())
        }

        button(kX) {
            changeOn(StartWinchCommand())
            changeOff(StopWinchCommand())
        }

        button(kB) {
            changeOn(StartWinchCommand(reversed = true))
            changeOff(StopWinchCommand())
        }

        pov(POVSide.DOWN).changeOn(SetElevatorPositionCommand(ElevatorSubsystem.Position.MINIMUM, instant = true))
        pov(POVSide.RIGHT).changeOn(SetElevatorPositionCommand(ElevatorSubsystem.Position.CARRY, instant = true))
        pov(POVSide.LEFT).changeOn(SetElevatorPositionCommand(ElevatorSubsystem.Position.SWITCH, instant = true))
        pov(POVSide.UP).changeOn(SetElevatorPositionCommand(ElevatorSubsystem.Position.SCALE, instant = true))
    }

    val turningAxis: Double
        get() {
            val axis = driveJoystick.getX(GenericHID.Hand.kLeft)
            return if (axis in 0.0..0.2) {
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
