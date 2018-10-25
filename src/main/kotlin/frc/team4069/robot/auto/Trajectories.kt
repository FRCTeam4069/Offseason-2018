package frc.team4069.robot.auto

import jaci.pathfinder.Pathfinder
import openrio.powerup.MatchData
import java.io.File

object Trajectories {
    private val paths = File("/home/lvuser/paths").listFiles { file -> file.extension == "csv" }
        .associate {
            val parts = it.nameWithoutExtension.split("-").map(String::toLowerCase)

            val gamePiece = when(parts[0]) {
                "switch" -> MatchData.GameFeature.SWITCH_NEAR
                "scale" -> MatchData.GameFeature.SCALE
                else -> throw IllegalArgumentException("Unknown game piece ${parts[0]}")
            }

            val side = when(parts[1]) {
                "left" -> MatchData.OwnedSide.LEFT
                "right" -> MatchData.OwnedSide.RIGHT
                else -> throw IllegalArgumentException("Unknown side ${parts[1]}")
            }

            val far = when(parts.getOrNull(2)) {
                "far" -> true
                else -> false
            }


            (gamePiece to side and far) to Pathfinder.readFromCSV(it)
        }

    init {
        println("TRAJS: $paths")
    }

    operator fun get(feature: MatchData.GameFeature, side: MatchData.OwnedSide, far: Boolean) = paths[feature to side and far]
    operator fun get(feature: MatchData.GameFeature, side: MatchData.OwnedSide) = get(feature, side, false)

    infix fun <A, B, C> Pair<A, B>.and(third: C) = Triple(first, second, third)
}