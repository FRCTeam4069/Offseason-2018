package frc.team4069.robot.auto

import frc.team4069.saturn.lib.math.geometry.Pose2d
import frc.team4069.saturn.lib.math.geometry.Pose2dWithCurvature
import frc.team4069.saturn.lib.math.geometry.deg
import frc.team4069.saturn.lib.math.trajectory.DefaultTrajectoryGenerator
import frc.team4069.saturn.lib.math.trajectory.TimedTrajectory
import frc.team4069.saturn.lib.math.trajectory.constraint.Constraint
import frc.team4069.saturn.lib.math.trajectory.constraint.NyoomyCirclyConstraint
import frc.team4069.saturn.lib.math.uom.distance.ft
import frc.team4069.saturn.lib.math.uom.velocity.VelocityUnit
import frc.team4069.saturn.lib.math.uom.velocity.fps

object Trajectories {

    internal val trajectories = mutableListOf<Container>()

    private val maxVelocity = 8.fps
    private const val maxAcceleration = 5.0
    private val maxCentripetalAcceleration = 4.0

    private val constraints = arrayListOf<Constraint<Pose2dWithCurvature>>(
        NyoomyCirclyConstraint(maxCentripetalAcceleration)
    )

    val switchRight = waypoints {
        +Pose2d(0.ft, 13.ft, 0.deg)
        +Pose2d(4.ft, 12.ft, (-45).deg)
        +Pose2d(11.ft, 8.ft, 0.deg)
    }.generateTrajectory("switch-right", false)


    private class Waypoints {
        val points = mutableListOf<Pose2d>()

        fun generateTrajectory(
            name: String,
            reversed: Boolean,
            maxVelocity: VelocityUnit = Trajectories.maxVelocity,
            maxAcceleration: Double = Trajectories.maxAcceleration,
            constraints: ArrayList<Constraint<Pose2dWithCurvature>> = Trajectories.constraints
        ): TimedTrajectory<Pose2dWithCurvature> {

            return DefaultTrajectoryGenerator.generateTrajectory(
                reversed = reversed,
                waypoints = points,
                constraints = constraints,
                startVelocity = 0.fps,
                endVelocity = 0.fps,
                maxVelocity = maxVelocity,
                maxAcceleration = maxAcceleration
            )
        }

        operator fun Pose2d.unaryPlus() {
            points.add(this)
        }
    }

    private inline fun waypoints(block: Waypoints.() -> Unit): Waypoints = Waypoints().apply(block)

    data class Container(val name: String, val trajectory: TimedTrajectory<Pose2dWithCurvature>)
}
