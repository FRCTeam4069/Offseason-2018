import edu.wpi.first.gradlerio.GradleRIOPlugin
import edu.wpi.first.gradlerio.frc.FRCJavaArtifact
import edu.wpi.first.gradlerio.frc.FRCPlugin
import edu.wpi.first.gradlerio.frc.RoboRIO
import edu.wpi.first.gradlerio.wpi.dependencies.WPIVendorDepsExtension
import edu.wpi.first.toolchain.NativePlatforms
import jaci.gradle.deploy.artifact.ArtifactsExtension
import jaci.gradle.deploy.artifact.FileTreeArtifact
import jaci.gradle.deploy.target.TargetsExtension

plugins {
    kotlin("jvm") version "1.3.11"
    id("edu.wpi.first.GradleRIO") version "2019.1.1"
}

val kMainRobotClass = "frc.team4069.robot.RobotKt"

deploy {
    targets {
        roboRIO()
    }
    artifacts {
        frcJavaArtifact()
        frcStaticFileDeploy()
    }
}

repositories {
    jcenter()
    maven { setUrl("http://dl.bintray.com/kyonifer/maven") }
    maven { setUrl("https://jitpack.io") }
    maven { setUrl("https://github.com/rosjava/rosjava_mvn_repo/raw/master") }
}

dependencies {
    // Kotlin Standard Library and Coroutines
    compile(kotlin("stdlib"))
    compile("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.1.0")

    // SaturnLib
    compile("com.github.FRCTeam4069:SaturnLibrary:fc96a69a09")

    // WPILib
    wpi.deps.wpilib().forEach { compile(it) }
    wpi.deps.vendor.java().forEach { compile(it) }
    wpi.deps.vendor.jni(NativePlatforms.roborio).forEach { nativeZip(it) }
    wpi.deps.vendor.jni(NativePlatforms.desktop).forEach { nativeDesktopZip(it) }
}

tasks.jar {
    doFirst {
        from(configurations.compile.get().map {
            if(it.isDirectory) it else zipTree(it)
        })

        manifest(GradleRIOPlugin.javaManifest(kMainRobotClass))
    }
}

tasks.withType<Wrapper>().configureEach {
    gradleVersion = "5.0"
}

fun TargetsExtension.roboRIO() =
    target("roboRIO", RoboRIO::class.java, closureOf<RoboRIO> { team = 4069; timeout = 1000 })

fun ArtifactsExtension.frcJavaArtifact() =
    artifact("frcJava", FRCJavaArtifact::class.java, closureOf<FRCJavaArtifact> { targets.add("roboRIO") })

fun ArtifactsExtension.frcStaticFileDeploy() =
    fileTreeArtifact("frcStaticFileDeploy", closureOf<FileTreeArtifact> {
        setFiles(fileTree("src/main/deploy"))
        targets.add("roboRIO")
        directory = "/home/lvuser/deploy"
    })

