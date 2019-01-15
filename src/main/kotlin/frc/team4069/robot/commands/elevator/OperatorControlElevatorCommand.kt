package frc.team4069.robot.commands.elevator

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.command.Command
import frc.team4069.robot.OI
import frc.team4069.robot.subsystems.ElevatorSubsystem
import frc.team4069.saturn.lib.commands.SaturnCommand

class OperatorControlElevatorCommand : SaturnCommand(ElevatorSubsystem) {

    private var set = true

    override suspend fun initialize() {
        println("Operator command init")
    }

    override suspend fun execute() {
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
}
