package frc.team4069.robot

import frc.team4069.robot.commands.OperatorControlCommandGroup
import frc.team4069.robot.commands.drive.DriveStraightCommand2
import frc.team4069.saturn.lib.SaturnRobot

class Robot : SaturnRobot() {

    override fun autonomousInit() {
        scheduler.add(DriveStraightCommand2(2.0))
    }

    override fun teleopInit() {
        scheduler.removeAll()
        scheduler.add(OperatorControlCommandGroup())
    }

    override fun autonomousPeriodic() {
        scheduler.run()
    }

    override fun teleopPeriodic() {
        scheduler.run()
    }
}

