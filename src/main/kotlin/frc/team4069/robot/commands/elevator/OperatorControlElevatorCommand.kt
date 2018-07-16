package frc.team4069.robot.commands.elevator

import com.ctre.phoenix.motorcontrol.ControlMode
import frc.team4069.robot.OI
import frc.team4069.robot.subsystems.ElevatorSubsystem
import frc.team4069.saturn.lib.command.Command

class OperatorControlElevatorCommand : Command() {

    private var set = true

    init {
        requires(ElevatorSubsystem)
    }

    override fun periodic() {
        val elevatorAxis = OI.elevatorAxis * 0.8

        if(elevatorAxis != 0.0) {
            ElevatorSubsystem.set(ControlMode.PercentOutput, elevatorAxis)
            set = false
        }else {
            if(!set) {
                ElevatorSubsystem.set(ControlMode.MotionMagic, ElevatorSubsystem.position.toDouble())
                set = true
            }
        }
    }

    override val isFinished: Boolean = false
}
