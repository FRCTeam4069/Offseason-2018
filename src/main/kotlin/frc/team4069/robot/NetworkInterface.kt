package frc.team4069.robot

import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.Notifier
import frc.team4069.robot.commands.drive.FollowPathCommand

object NetworkInterface {
    val INSTANCE = NTConnection.getTable("Live Dashboard")

    private val robotX = INSTANCE["Robot X"]
    private val robotY = INSTANCE["Robot Y"]
    private val robotHdg = INSTANCE["Robot Heading"]

    private val pathX = INSTANCE["Path X"]
    private val pathY = INSTANCE["Path Y"]
    private val pathHdg = INSTANCE["Path Heading"]

    private val isEnabled = INSTANCE["Is Enabled"]

    private val notifier: Notifier

    init {
        notifier = Notifier {
            val pose = Localization.position
            robotX.setDouble(pose.x)
            robotY.setDouble(pose.y)
            robotHdg.setDouble(pose.theta)

            pathX.setDouble(FollowPathCommand.pathX)
            pathY.setDouble(FollowPathCommand.pathY)
            pathHdg.setDouble(FollowPathCommand.pathHdg)

            isEnabled.setBoolean(DriverStation.getInstance().isEnabled)
        }

        notifier.startPeriodic(0.02)
    }

    fun stopTracking() {
        notifier.stop()
    }
}