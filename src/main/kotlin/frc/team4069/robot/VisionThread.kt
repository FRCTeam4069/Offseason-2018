package frc.team4069.robot

import edu.wpi.first.wpilibj.CameraServer
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

fun startVisionThread() {
    Thread {
        val server = CameraServer.getInstance()
        val camera = server.startAutomaticCapture()
        camera.setResolution(640, 480)
        val sink = server.getVideo()
        val outputStream = server.putVideo("Blur", 640, 480)
        val source = Mat()
        val output = Mat()
        while (!Thread.interrupted()) {
            sink.grabFrame(source)
            Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY)
            outputStream.putFrame(output)
        }
    }.start()
}