package frc.team4069.robot.vision

import edu.wpi.first.wpilibj.CameraServer
import edu.wpi.first.wpilibj.vision.VisionRunner
import edu.wpi.first.wpilibj.vision.VisionThread
import org.opencv.imgproc.Imgproc



object VisionSystem {
    val imgLock = Object()
    fun startVisionThread() {
        val camera = CameraServer.getInstance().startAutomaticCapture()
        camera.setResolution(640, 480)
        val visionThread = VisionThread(camera, BlurPipeline(), VisionRunner.Listener { pipeline: BlurPipeline ->
            val r = Imgproc.boundingRect(pipeline.blurOutput())
            synchronized(imgLock) {
                // do some stuff
            }
        })
    }
}
