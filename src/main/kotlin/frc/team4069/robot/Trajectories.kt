package frc.team4069.robot

import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2d
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2dWithCurvature
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.DefaultTrajectoryGenerator
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.constraints.CentripetalAccelerationConstraint
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.constraints.TimingConstraint
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.types.TimedTrajectory
import frc.team4069.saturn.lib.mathematics.units.degree
import frc.team4069.saturn.lib.mathematics.units.derivedunits.Acceleration
import frc.team4069.saturn.lib.mathematics.units.derivedunits.Velocity
import frc.team4069.saturn.lib.mathematics.units.derivedunits.acceleration
import frc.team4069.saturn.lib.mathematics.units.derivedunits.velocity
import frc.team4069.saturn.lib.mathematics.units.feet
import frc.team4069.saturn.lib.mathematics.units.meter

object Trajectories {
    private val maxVelocity = 8.feet.velocity
    private val maxAcceleration = 5.5.feet.acceleration
    private val maxCentripedalAcceleration = 4.feet.acceleration

    private val constraints = listOf(
        CentripetalAccelerationConstraint(maxCentripedalAcceleration)
    )

    val switch = waypoints(
        Pose2d(0.feet, 13.feet, 0.degree),
        Pose2d(4.feet, 12.feet, (-45).degree),
        Pose2d(11.feet, 8.feet, 0.degree)
    ).generateTrajectory("Switch Right", reversed = false, maxVelocity = 4.feet.velocity,
        maxAcceleration = 3.feet.acceleration,
        constraints = listOf(
            CentripetalAccelerationConstraint(2.feet.acceleration)
        ))

    private fun waypoints(vararg waypoints: Pose2d) = listOf(*waypoints)

    private fun List<Pose2d>.generateTrajectory(
        name: String,
        reversed: Boolean,
        maxVelocity: Velocity = Trajectories.maxVelocity,
        maxAcceleration: Acceleration = Trajectories.maxAcceleration,
        constraints: List<TimingConstraint<Pose2dWithCurvature>> = Trajectories.constraints
    ): TimedTrajectory<Pose2dWithCurvature> {
        println("Generating $name")
        return DefaultTrajectoryGenerator.generateTrajectory(
            reversed = reversed,
            wayPoints = this,
            constraints = constraints,
            startVelocity = 0.meter.velocity,
            endVelocity = 0.meter.velocity,
            maxVelocity = maxVelocity,
            maxAcceleration = maxAcceleration
        )
    }
}