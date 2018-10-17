package frc.team4069.robot.vision

import edu.wpi.first.wpilibj.CameraServer
import edu.wpi.first.wpilibj.vision.VisionRunner
import edu.wpi.first.wpilibj.vision.VisionThread
import kotlinx.coroutines.experimental.launch
import org.opencv.imgproc.Imgproc


object VisionSystem {
    var tapeX = 0
    var tapeY = 0
    private val imgLock = Object()
    val width = 640
    val height = 480
    fun startVisionThread() {
        val camera = CameraServer.getInstance().startAutomaticCapture()
        camera.setResolution(width, height)
        val visionThread = VisionThread(camera, TapePipeline(), VisionRunner.Listener { pipeline ->
            val contours = pipeline.filterContoursOutput()
            synchronized(imgLock) {
                val numContours = contours.count()
                if (numContours >= 1) {
                    var totalX = 0
                    var totalY = 0
                    for (contourIndex in 0 until numContours) {
                        val boundingRect = Imgproc.boundingRect(pipeline.filterContoursOutput().get(contourIndex))
                        totalX += boundingRect.x + (boundingRect.width / 2)
                        totalY += boundingRect.y + (boundingRect.height / 2)
                    }
                    tapeX = (totalX / numContours) - (width / 2)
                    tapeY = (totalY / numContours) - (height / 2)
                    println("X:")
                    println(tapeX)
                    println("Y:")
                    println(tapeY)

                }

            }
        })
        visionThread.start()
    }
}
