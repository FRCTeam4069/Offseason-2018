package frc.team4069.robot

import frc.team4069.saturn.lib.pneumatics.Compressor

object Pneumatics {
    val compressor = Compressor(0)

    fun enable() {
        compressor.start()
    }

    fun disable() {
        compressor.stop()
    }
}