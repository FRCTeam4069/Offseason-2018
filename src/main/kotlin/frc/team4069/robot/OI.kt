package frc.team4069.robot

import edu.wpi.first.wpilibj.GenericHID
import frc.team4069.robot.commands.drive.DriveCommand
import frc.team4069.saturn.lib.hid.button
import frc.team4069.saturn.lib.hid.kA
import frc.team4069.saturn.lib.hid.xboxController

object OI {

//    private val driveJoystick: Controller
//    val controlJoystick: Controller

    val driveJoystick = xboxController(0) {
        button(kA) {
            val cmd = DriveCommand(DriveCommand.Direction.FORWARDS)
            changeOn(cmd)
            changeOff { cmd.stop() }
        }
    }

    val controlJoystick = xboxController(1) {}

//    init {
//
//    }

    val turningAxis: Double
        get() {
            val axis = driveJoystick.inner.getX(GenericHID.Hand.kLeft)
            return if(axis in 0.0..0.2) {
                0.0
            }else {
                axis
            }
        }

    val driveSpeed: Double
        get() {
            val forward = driveJoystick.inner.getTriggerAxis(GenericHID.Hand.kRight)
            val backward = driveJoystick.inner.getTriggerAxis(GenericHID.Hand.kLeft)

            return forward - backward
        }

    val elevatorAxis: Double
        get() {
            val axis = controlJoystick.inner.getY(GenericHID.Hand.kRight)
            return if(Math.abs(axis) in 0.0..0.2) {
                0.0
            }else {
                axis
            }
        }

    val intakeAxis: Double
        get() {
            val forward = controlJoystick.inner.getRawAxis(3)
            val backward = controlJoystick.inner.getRawAxis(2)

            return forward - backward
        }
}
