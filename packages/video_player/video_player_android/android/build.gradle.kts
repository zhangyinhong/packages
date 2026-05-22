import org.jetbrains.kotlin.gradle.dsl.JvmTarget

group = "io.flutter.plugins.videoplayer"
version = "1.0-SNAPSHOT"

buildscript {
    val kotlinVersion = "2.3.0"
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.13.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("com.android.library")
    id("kotlin-android")
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget(JavaVersion.VERSION_17.toString())
    }
}

android {
    namespace = "io.flutter.plugins.videoplayer"
    compileSdk = flutter.compileSdkVersion

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    lint {
        checkAllWarnings = true
        warningsAsErrors = true
        disable.addAll(setOf("AndroidGradlePluginVersion", "InvalidPackage", "GradleDependency", "NewerVersionAvailable"))
        baseline = file("lint-baseline.xml")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

   dependencies {
        // 使用 ExoPlayer 2.18.7 替代 Media3
        val exoplayerVersion = "2.18.7"
        implementation("com.google.android.exoplayer:exoplayer:${exoplayerVersion}")
        implementation("com.google.android.exoplayer:exoplayer-hls:${exoplayerVersion}")
        implementation("com.google.android.exoplayer:exoplayer-dash:${exoplayerVersion}")
        implementation("com.google.android.exoplayer:exoplayer-rtsp:${exoplayerVersion}")
        implementation("com.google.android.exoplayer:exoplayer-smoothstreaming:${exoplayerVersion}")
        
        testImplementation("junit:junit:4.13.2")
        testImplementation("androidx.test:core:1.7.0")
        testImplementation("org.mockito:mockito-core:5.23.0")
        testImplementation("org.robolectric:robolectric:4.16")
        testImplementation("com.google.android.exoplayer:exoplayer-robolectric:${exoplayerVersion}")
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
            all {
                it.outputs.upToDateWhen { false }
                it.testLogging {
                    events("passed", "skipped", "failed", "standardOut", "standardError")
                    showStandardStreams = true
                }
                // The org.gradle.jvmargs property that may be set in gradle.properties does not impact
                // the Java heap size when running the Android unit tests. The following property here
                // sets the heap size to a size large enough to run the robolectric tests across
                // multiple SDK levels.
                it.jvmArgs("-Xmx4G")
            }
        }
    }
}
