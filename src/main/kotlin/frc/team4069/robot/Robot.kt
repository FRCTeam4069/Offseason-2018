package frc.team4069.robot

import edu.wpi.first.wpilibj.ADXRS450_Gyro
import edu.wpi.first.wpilibj.SPI
import edu.wpi.first.wpilibj.command.Scheduler
import edu.wpi.first.wpilibj.livewindow.LiveWindow
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team4069.robot.auto.AutoMode
import frc.team4069.robot.auto.Trajectories
import frc.team4069.robot.auto.modes.LeftScaleMode
import frc.team4069.robot.auto.modes.RightScaleMode
import frc.team4069.robot.auto.modes.SwitchAutoMode
import frc.team4069.robot.commands.OperatorControlCommandGroup
import frc.team4069.robot.subsystems.ArmSubsystem
import frc.team4069.robot.subsystems.DriveBaseSubsystem
import frc.team4069.robot.subsystems.ElevatorSubsystem
import frc.team4069.robot.subsystems.IntakeSubsystem
import frc.team4069.saturn.lib.SaturnRobot

class Robot : SaturnRobot() {
    lateinit var autoChooser: SendableChooser<AutoMode>

    override fun robotInit() {
        LiveWindow.disableAllTelemetry()
        Localization
        NTConnection
        NetworkInterface

        // Load in the trajectories from disk
        Trajectories

        +OI.driveJoystick
        +OI.controlJoystick

        autoChooser = SendableChooser<AutoMode>().apply {
            addObject("Center", SwitchAutoMode())
            addObject("Left", LeftScaleMode())
            addObject("Right", RightScaleMode())
        }

        SmartDashboard.putData("Starting positions", autoChooser)

//         Subsystem initialization
        DriveBaseSubsystem
        ArmSubsystem
        ElevatorSubsystem
        IntakeSubsystem


        DriveBaseSubsystem.reset()
    }

    override fun autonomousInit() {
        Scheduler.getInstance().add(autoChooser.selected.build())
    }

    override fun teleopInit() {
        Scheduler.getInstance().add(OperatorControlCommandGroup())
    }

    override fun disabledInit() {
        IntakeSubsystem.disableSolenoid()
//        NetworkInterface.stopTracking()
    }

    override fun notifyBrownout() {
        Pneumatics.disable()

        DriveBaseSubsystem.reduceLimits()
        ElevatorSubsystem.reduceLimits()

        SmartDashboard.putBoolean("Brownout Occurred", true)
    }

    companion object {
        val gyro by lazy {
            ADXRS450_Gyro(SPI.Port.kOnboardCS0)
        }
    }
}
