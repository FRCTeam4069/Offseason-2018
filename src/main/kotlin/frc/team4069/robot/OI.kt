package frc.team4069.robot

import edu.wpi.first.wpilibj.GenericHID
import frc.team4069.robot.commands.arm.DeployArmCommand
import frc.team4069.robot.commands.arm.StartArmCommand
import frc.team4069.robot.commands.drive.DriveCommand
import frc.team4069.robot.commands.elevator.SetElevatorPositionCommand
import frc.team4069.robot.commands.intake.SetIntakeSpeedCommand
import frc.team4069.robot.subsystems.ElevatorSubsystem
import frc.team4069.saturn.lib.hid.ButtonType.*
import frc.team4069.saturn.lib.hid.Controller
import frc.team4069.saturn.lib.hid.controller

object OI {

    private val driveJoystick: Controller
    private val controlJoystick: Controller

    init {
        controlJoystick = controller(1) {
            button(A) {
                val command = SetIntakeSpeedCommand(-0.5)
                pressed(command)
                released { command.stop() }
            }

            button(Y) {
                pressed(SetElevatorPositionCommand(ElevatorSubsystem.Position.SCALE))
            }
        }

        driveJoystick = controller(0) {
            button(BUMPER_RIGHT)  {
                val command = DriveCommand(DriveCommand.Direction.FORWARDS)

                pressed(command)
                released { command.stop() }
            }

            button(Y) {
                pressed(DeployArmCommand())
            }

            button(A) {
                val command = StartArmCommand(true)

                pressed(command)
                released { command.stop() }
            }
        }
    }

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
