package frc.team4069.robot.commands.drive

import edu.wpi.first.wpilibj.command.InstantCommand
import edu.wpi.first.wpilibj.command.Scheduler
import frc.team4069.saturn.lib.math.uom.distance.ft
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.runBlocking

class DeferredDriveStraightCommand(val bicc: Deferred<Double>) : InstantCommand() {

    override fun initialize() {
        runBlocking {
            Scheduler.getInstance().add(DriveStraightCommand((-bicc.await()).ft))
        }
    }
}
