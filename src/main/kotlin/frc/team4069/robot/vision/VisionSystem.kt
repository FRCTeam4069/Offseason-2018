package frc.team4069.robot.vision

import edu.wpi.first.wpilibj.CameraServer
import edu.wpi.first.wpilibj.vision.VisionRunner
import edu.wpi.first.wpilibj.vision.VisionThread
import org.opencv.imgproc.Imgproc
import kotlin.math.sqrt


object VisionSystem {
    var tapeX = 0
    var tapeY = 0
    private val sideLengthToDistanceTable = listOf(Pair(186, 1))
    private val imgLock = Object()
    private var pastSideLengths = DoubleArray(4)
    val width = 1280
    val height = 720
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
                        val area = boundingRect.area()
                        val currentSideLength = sqrt(area)
                        for (i in 0 until pastSideLengths.count() - 1) {
                            pastSideLengths[i] = pastSideLengths[i + 1]
                        }
                        pastSideLengths[pastSideLengths.count() - 1] = currentSideLength
                        val sideLength = pastSideLengths.average()
                        for (i in 0 until sideLengthToDistanceTable.count()) {
                            val (storedSideLength, storedDistance) = sideLengthToDistanceTable[i]
                            if (storedSideLength > sideLength) {
                                println(i)
                            }
                        }
                        println(sideLength)
                    }
                    tapeX = (totalX / numContours) - (width / 2)
                    tapeY = (totalY / numContours) - (height / 2)
//                    println("X:")
//                    println(tapeX)
//                    println("Y:")
//                    println(tapeY)

                }

            }
        })
        visionThread.start()
    }
}
