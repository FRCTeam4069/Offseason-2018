package frc.team4069.robot.commands.drive

import com.ctre.phoenix.motorcontrol.ControlMode
import com.team254.lib.physics.DifferentialDrive
import edu.wpi.first.wpilibj.command.Command
import frc.team4069.robot.Constants
import frc.team4069.robot.Localization
import frc.team4069.saturn.lib.mathematics.VelocityPIDFController
import frc.team4069.saturn.lib.mathematics.twodim.control.RamseteController
import frc.team4069.saturn.lib.mathematics.twodim.geometry.Pose2dWithCurvature
import frc.team4069.saturn.lib.mathematics.twodim.trajectory.types.TimedTrajectory
import frc.team4069.saturn.lib.mathematics.units.Length
import frc.team4069.saturn.lib.mathematics.units.derivedunits.feetPerSecond
import frc.team4069.saturn.lib.mathematics.units.feet
import frc.team4069.robot.subsystems.DriveBaseSubsystem as driveBase

class FollowPathCommand(
    path: TimedTrajectory<Pose2dWithCurvature>,
    zeroPose: Boolean = false
) : Command() {
    private val follower = RamseteController(path, Constants.kB, Constants.kZeta)

    private val lController = VelocityPIDFController(
        p = Constants.DRIVETRAIN_P,
        d = Constants.DRIVETRAIN_D,
        v = Constants.DRIVETRAIN_V,
        s = Constants.DRIVETRAIN_S,
        currentVelocity = { driveBase.leftVelocity.feetPerSecond }
    )

    private val rController = VelocityPIDFController(
        p = Constants.DRIVETRAIN_P,
        d = Constants.DRIVETRAIN_D,
        v = Constants.DRIVETRAIN_V,
        s = Constants.DRIVETRAIN_S,
        currentVelocity = { driveBase.rightVelocity.feetPerSecond }
    )

    init {
        println("Ramsete starting")
        requires(driveBase)

        if (zeroPose) {
            Localization.reset(path.firstState.state.pose)
            println("Pos: ${Localization.position}")
        }
    }

    override fun initialize() {
        driveBase.stop()
    }

    override fun execute() {
        val currentPose = Localization.position

        val state = follower.update(currentPose)

        val wheelState = inverseKinematics(state, Constants.DRIVETRAIN_WIDTH_FT.feet)

        // Target acceleration values can be 0.0 because this model has no kA feedforward
        val leftOut = lController.getPIDFOutput(wheelState.left, 0.0)
        val rightOut = rController.getPIDFOutput(wheelState.right, 0.0)

        updateDashboard()

        driveBase.set(
            ControlMode.PercentOutput,
            leftOut,
            rightOut
        )
    }

    override fun end() {
        driveBase.stop()
        println("Ending pose is ${Localization.position}")
    }

    override fun isFinished(): Boolean {
        return follower.isFinished
    }

    private fun updateDashboard() {
        val seg = follower.referencePose

        pathX = seg.translation.x.feet
        pathY = seg.translation.y.feet
        pathHdg = seg.rotation.radian
    }

    fun inverseKinematics(state: DifferentialDrive.ChassisState,
                          wheelBase: Length): DifferentialDrive.WheelState {
        val left = ((-wheelBase.feet * state.angular + 2 * state.linear) / 2)
        val right = ((wheelBase.feet * state.angular + 2 * state.linear) / 2)

        return DifferentialDrive.WheelState(left, right)
    }

    companion object {
        var pathX = 0.0
            private set

        var pathY = 0.0
            private set

        var pathHdg = 0.0
            private set
    }
}