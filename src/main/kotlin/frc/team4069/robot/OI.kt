package frc.team4069.robot

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.Joystick

object OI {
    private val driveJoystick = Joystick(0)
    private val controlJoystick = Joystick(1)

//    init {
        // Slow outtake command
//        controlJoystick.button(ButtonType.BUMPER_RIGHT)
//                .whenPressed(SetIntakeSpeedCommand(-0.5))
//                .whenReleased(command(IntakeSubsystem::stop))
//    }

    val driveLeft: Double
        get() {
            return -driveJoystick.getRawAxis(1)
        }

    val driveSpeed: Double
        get() {
            return -controlJoystick.getRawAxis(1)
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
