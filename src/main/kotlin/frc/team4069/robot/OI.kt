package frc.team4069.robot

import edu.wpi.first.wpilibj.GenericHID
import frc.team4069.robot.commands.arm.DeployArmCommand
import frc.team4069.robot.commands.arm.StartArmCommand
import frc.team4069.robot.commands.drive.DriveCommand
import frc.team4069.robot.commands.elevator.SetElevatorPositionCommand
import frc.team4069.robot.commands.intake.SetIntakeSpeedCommand
import frc.team4069.robot.subsystems.ArmSubsystem
import frc.team4069.robot.subsystems.DriveBaseSubsystem
import frc.team4069.robot.subsystems.ElevatorSubsystem
import frc.team4069.robot.subsystems.IntakeSubsystem
import frc.team4069.saturn.lib.command
import frc.team4069.saturn.lib.hid.ButtonType
import frc.team4069.saturn.lib.hid.Controller

object OI {
    private val driveJoystick = Controller(0)
    private val controlJoystick = Controller(1)

    init {
        // Slow outtake command
        controlJoystick.button(ButtonType.BUMPER_RIGHT)
                .whenPressed(SetIntakeSpeedCommand(-0.5))
                .whenReleased(command(IntakeSubsystem::stop))

        controlJoystick.button(ButtonType.Y)
                .whenPressed(SetElevatorPositionCommand(ElevatorSubsystem.Position.HALFWAY))

        driveJoystick.button(ButtonType.BUMPER_RIGHT)
                .whenPressed(DriveCommand(DriveCommand.Direction.FORWARDS))
                .whenReleased(command(DriveBaseSubsystem::stop))
        driveJoystick.button(ButtonType.Y)
                .whenPressed(DeployArmCommand())
        driveJoystick.button(ButtonType.A)
                .whenPressed(StartArmCommand(true))
                .whenReleased(command(ArmSubsystem::stop))
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
