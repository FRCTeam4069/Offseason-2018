package frc.team4069.robot.vision

import edu.wpi.first.wpilibj.CameraServer
import edu.wpi.first.wpilibj.vision.VisionRunner
import edu.wpi.first.wpilibj.vision.VisionThread
import org.opencv.imgproc.Imgproc



object VisionSystem {
    var tapeX = 0
    var tapeY = 0
    val imgLock = Object()
    fun startVisionThread() {
        val camera = CameraServer.getInstance().startAutomaticCapture()
        camera.setResolution(640, 480)
        val visionThread = VisionThread(camera, TapePipeline(), VisionRunner.Listener { pipeline ->
            val boundingRect = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0))
            synchronized(imgLock) {
                tapeX = boundingRect.x + (boundingRect.width / 2)
                tapeY = boundingRect.y + (boundingRect.height / 2)
                println("X:")
                println(tapeX)
                println("Y:")
                println(tapeX)
            }
        })
    }
}
