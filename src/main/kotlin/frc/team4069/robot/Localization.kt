package frc.team4069.robot

import edu.wpi.first.wpilibj.Notifier
import frc.team4069.robot.subsystems.DriveBaseSubsystem
import frc.team4069.saturn.lib.math.geometry.Pose2d
import frc.team4069.saturn.lib.math.geometry.Translation2d
import frc.team4069.saturn.lib.math.geometry.rad
import frc.team4069.saturn.lib.math.uom.distance.ft
import kotlin.math.cos
import kotlin.math.sin

object Localization {
    private val lock = Any()

    private var prevL = 0.0
    private var prevR = 0.0

//    var position = Pose2d(0.0, 0.0, 0.0)
//        @Synchronized get
//        private set

    var position = Pose2d()

    init {
        reset()
        Notifier(::run).startPeriodic(0.01)
    }

    fun reset(resetPose: Pose2d = Pose2d()) {
        synchronized(lock) {
            position = resetPose
            prevL = DriveBaseSubsystem.leftPosition.ft
            prevR = DriveBaseSubsystem.rightPosition.ft
        }
    }

    private fun run() {
        synchronized(lock) {

            val currentL = DriveBaseSubsystem.leftPosition.ft
            val currentR = DriveBaseSubsystem.rightPosition.ft
            val heading = Math.toRadians(-Robot.gyro.angle)

            val deltaL = currentL - prevL
            val deltaR = currentR - prevR

            val dist = (deltaL + deltaR) / 2.0

            val x = dist * cos(heading)
            val y = dist * sin(heading)

            position = Pose2d(
                Translation2d(position.translation.x + x.ft,
                    position.translation.y + y.ft),
                heading.rad
            )

            prevL = currentL
            prevR = currentR
        }
    }
}
