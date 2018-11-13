package frc.team4069.robot

import frc.team4069.saturn.lib.mathematics.units.inch
import frc.team4069.saturn.lib.mathematics.units.nativeunits.NativeUnitLengthModel
import frc.team4069.saturn.lib.mathematics.units.nativeunits.STU

object Constants {
    const val DRIVETRAIN_P = 0.25
    const val DRIVETRAIN_D = 0.0001
    const val DRIVETRAIN_V = 0.07143
    const val DRIVETRAIN_S = 0.1
    const val DRIVETRAIN_WIDTH_FT = 5.5
    // Ramsete constants
    const val kZeta = 0.9
    const val kB = 1.0

    val DT_MODEL = NativeUnitLengthModel(256.STU, 3.6875.inch)
}