package frc.team4069.robot

import edu.wpi.first.wpilibj.Notifier
import frc.team4069.robot.subsystems.DriveBaseSubsystem
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2d
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Translation2d
import frc.team4069.saturn.lib.mathematics.units.degree
import frc.team4069.saturn.lib.mathematics.units.feet
import frc.team4069.saturn.lib.mathematics.units.radian
import kotlin.math.cos
import kotlin.math.sin

object Localization {
    private val lock = Any()

    private var prevL = 0.0
    private var prevR = 0.0

    var position = Pose2d(0.0.feet, 0.0.feet, 0.0.degree)
        @Synchronized get
        private set

    init {
        reset()
        Notifier(::run).startPeriodic(0.01)
    }

    fun reset(resetPose: Pose2d = Pose2d(0.0.feet, 0.0.feet, 0.0.degree)) {
        synchronized(lock) {
            position = resetPose
            prevL = DriveBaseSubsystem.leftPosition.toModel(Constants.DT_MODEL).feet
            prevR = DriveBaseSubsystem.rightPosition.toModel(Constants.DT_MODEL).feet
        }
    }

    private fun run() {
        synchronized(lock) {

            val currentL = DriveBaseSubsystem.leftPosition.toModel(Constants.DT_MODEL).feet
            val currentR = DriveBaseSubsystem.rightPosition.toModel(Constants.DT_MODEL).feet
            val heading = Math.toRadians(-Robot.gyro.angle)

            val deltaL = currentL - prevL
            val deltaR = currentR - prevR

            val dist = (deltaL + deltaR) / 2.0

            val x = dist * cos(heading)
            val y = dist * sin(heading)

            position = Pose2d(
                Translation2d(
                    position.translation.x + x.feet,
                    position.translation.y + y.feet
                ),
                heading.radian
            )

            prevL = currentL
            prevR = currentR
        }
    }
}
