package frc.team4069.robot

import frc.team4069.robot.commands.OperatorControlCommandGroup
import frc.team4069.saturn.lib.SaturnRobot
import frc.team4069.saturn.lib.command.Scheduler

class Robot : SaturnRobot() {

    override fun teleopInit() {
        Scheduler.clear()
        Scheduler.add(OperatorControlCommandGroup())
    }

    override fun autonomousPeriodic() {
        Scheduler.run()
    }

    override fun teleopPeriodic() {
        Scheduler.run()
    }
}
