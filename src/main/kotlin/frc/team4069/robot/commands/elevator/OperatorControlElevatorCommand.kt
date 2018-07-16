package frc.team4069.robot.commands.elevator

import com.ctre.phoenix.motorcontrol.ControlMode
import frc.team4069.robot.OI
import frc.team4069.saturn.lib.command.Command
import frc.team4069.robot.subsystems.ElevatorSubsystem as elevator

class OperatorControlElevatorCommand : Command() {

    private var set = true

    init {
        requires(elevator)
    }

    override fun periodic() {
        val elevatorAxis = OI.elevatorAxis * 0.8

        if(elevatorAxis != 0.0) {
            elevator.set(ControlMode.PercentOutput, elevatorAxis)
            set = false
        }else {
            if(!set) {
                elevator.set(ControlMode.MotionMagic, elevator.position.toDouble())
                set = true
            }
        }
    }

    override val isFinished: Boolean = false
}
