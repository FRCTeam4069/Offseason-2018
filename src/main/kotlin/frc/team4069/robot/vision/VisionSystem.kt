package frc.team4069.robot.vision

import edu.wpi.first.wpilibj.CameraServer
import edu.wpi.first.wpilibj.vision.VisionRunner
import edu.wpi.first.wpilibj.vision.VisionThread
import org.opencv.imgproc.Imgproc
import kotlin.math.sqrt


object VisionSystem {
    var tapeX = 0
    var tapeY = 0
    private val sideLengthToDistanceTable = listOf(
            Pair(40.0, 6.0),
            Pair(48.0, 5.0),
            Pair(60.0, 4.0),
            Pair(69.0, 3.5),
            Pair(78.0, 3.0),
            Pair(88.0, 2.7),
            Pair(97.0, 2.4),
            Pair(105.0, 2.2),
            Pair(113.0, 2.0),
            Pair(125.0, 1.8),
            Pair(141.0, 1.6),
            Pair(164.0, 1.4),
            Pair(178.0, 1.2),
            Pair(197.0, 1.0),
            Pair(223.0, 0.8),
            Pair(268.0, 0.6)
    )
    private val imgLock = Object()
    private var pastSideLengths = DoubleArray(4)
    val width = 1280
    val height = 720
    var distance = -1.0
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

                    }
                    val sideLength = pastSideLengths.average()
                    for (i in 0 until sideLengthToDistanceTable.count() - 1) {
                        val (storedSideLength, storedDistance) = sideLengthToDistanceTable[i]
                        val (nextStoredSideLength, nextStoredDistance) = sideLengthToDistanceTable[i + 1]
                        if (storedSideLength < sideLength && nextStoredSideLength > sideLength) {
                            val sideLengthDelta = nextStoredSideLength - storedSideLength
                            val distanceDelta = nextStoredDistance - storedDistance
                            val a = (sideLength - storedSideLength) / sideLengthDelta
                            distance = storedDistance + (distanceDelta * a)
                        }
                    }
                    println(distance)
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
